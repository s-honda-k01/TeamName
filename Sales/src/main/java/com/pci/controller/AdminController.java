package com.pci.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.pci.entity.MtRole;
import com.pci.entity.MtUser;
import com.pci.repository.RoleRepository;
import com.pci.repository.UserRepository;
import com.pci.security.UserAccountService;

@Controller
@SessionAttributes("formModel")
public class AdminController {
	UserDetails userDetail;
	MtUser user;
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	private UserAccountService sv;
	
	@ModelAttribute(value = "formModel")
	public MtUser setUpMtUser() {
		return new MtUser(); 
	}
	
	@RequestMapping(value = "/admin/userList",method=RequestMethod.GET)
	public ModelAndView userList(ModelAndView mav,@AuthenticationPrincipal UserDetails userDetail) {
		this.userDetail=userDetail;
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		Iterable<MtUser> userList = userRepository.findAll();
		mav.addObject("userList", userList);
		mav.setViewName("/100admin/110userList");
		return mav;
	}
	
	@RequestMapping(value = "/admin/userCre",method=RequestMethod.POST)
	public ModelAndView userCre(ModelAndView mav) {
		mav.addObject("userName", this.user.getUserName());
		MtUser user = new MtUser();
		mav.addObject("formModel", user);
		mav.addObject("roleList", getRoleList(roleRepository.findAll()));
		mav.setViewName("/100admin/111userCre");
		return mav;
	}
	
	@RequestMapping(value = "/userCreConf",method=RequestMethod.POST)
	public ModelAndView userCreConf(
			@ModelAttribute("formModel") @Validated MtUser user,
			BindingResult result,
			ModelAndView mav) {
		mav.addObject("userName", this.user.getUserName());
		Optional<MtUser> exist = userRepository.findByUserCode(user.getUserCode());
		if(!result.hasErrors()) {
			if(exist.isEmpty()) {
				mav.setViewName("/100admin/112userCreConf");
			}else {
				mav.addObject("msg", "そのユーザーコードはすでに使われています");
				mav.addObject("roleList", getRoleList(roleRepository.findAll()));
				mav.setViewName("/100admin/111userCre");				
			}
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.addObject("roleList", getRoleList(roleRepository.findAll()));
			mav.setViewName("/100admin/111userCre");		
		}
		return mav;
	}
	
	@RequestMapping(value = "/userRegExe",method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView userRegExe(
			@ModelAttribute("formModel")MtUser user,
			ModelAndView mav) {
		mav.addObject("userName", this.user.getUserName());
		switch(user.getMtRole().getRoleName()) {
		case "admin":
			sv.registerAdmin(user.getUserCode(), user.getUserName(), user.getPassword(),user.getValidity());
			break;
		case "manager":
			sv.registerManager(user.getUserCode(), user.getUserName(), user.getPassword(),user.getValidity());
			break;
		case "staff":
			sv.registerStaff(user.getUserCode(), user.getUserName(), user.getPassword(),user.getValidity());
			break;
		}
		mav.setViewName("redirect:/user?list");
		return mav;
	}
	
	@RequestMapping(value = "/userUpd/{userCode}",method = RequestMethod.GET)
	public ModelAndView userUpd(
			@PathVariable String userCode,
			ModelAndView mav) {
		mav.addObject("userName", this.user.getUserName());
		mav.addObject("roleList", getRoleList(roleRepository.findAll()));
		Optional<MtUser> user = userRepository.findByUserCode(userCode);
		mav.addObject("formModel", user.get());
		mav.setViewName("/100admin/115userUpd");
		return mav;
	}
	
	@RequestMapping(value = "/userUpdConf",method = RequestMethod.POST)
	public ModelAndView userUpdConf(
			@ModelAttribute("formModel")MtUser user,
			BindingResult result,
			ModelAndView mav) {
		mav.addObject("userName", this.user.getUserName());
		if(!result.hasErrors()) {
			mav.setViewName("100admin/116userUpdConf");
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.addObject("roleList", getRoleList(roleRepository.findAll()));
			mav.setViewName("100admin/115userUpd");
		}
		return mav;
	}
	
	@RequestMapping(value = "/userDel/{userCode}",method=RequestMethod.GET)
	public ModelAndView userDel(
			@PathVariable String userCode,
			ModelAndView mav) {
		mav.addObject("userName", this.user.getUserName());
		Iterable<MtRole> roleList = roleRepository.findAll();
		mav.addObject("roleList", roleList);
		Optional<MtUser> user = userRepository.findByUserCode(userCode);
		mav.addObject("formModel", user.get());
		mav.setViewName("100admin/140userDelConf");
		return mav;
	}
	
	@RequestMapping(value = "/userDelExe",method=RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView userDelExe(
			@RequestParam String userCode,
			ModelAndView mav) {
		mav.addObject("userName", this.user.getUserName());
		userRepository.deleteById(userCode);
		mav.setViewName("redirect:/user?list");
		return mav;
	}

	@RequestMapping(value = "/user",method=RequestMethod.GET,params="list")
	public String saveComplete(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/admin/userList";
	}
	
	private boolean isManagerExist() {
		List<MtUser> userList = userRepository.getManagerList();
		int size = userList.size();
		System.out.println(size);
		if(userList.size()==0) {return false;}
		else {return true;}
	}
	
	private List<MtRole> getRoleList(List<MtRole> roleList){
		Iterator<MtRole> iter = roleList.iterator();
		while(iter.hasNext()) {
			if(iter.next().getRoleName().equals("admin")) {
				iter.remove();
			}else if(iter.next().getRoleName().equals("manager")){
				if(isManagerExist()==true) {iter.remove();}
			}
		}
		return roleList;
	}
}
