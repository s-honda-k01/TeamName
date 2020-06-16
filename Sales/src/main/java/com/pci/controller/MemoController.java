package com.pci.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.pci.entity.MtUser;
import com.pci.entity.TrMemo;
import com.pci.entity.TrMemoShareMember;
import com.pci.repository.MemoRepository;
import com.pci.repository.MemoShareMemberRepository;
import com.pci.repository.UserRepository;

@Controller
@SessionAttributes("formModel")
public class MemoController {
	
	
	UserDetails userDetail;
	MtUser user;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MemoRepository memoRepository;
	@Autowired
	MemoShareMemberRepository memoShareMemberRepository;
	
	List<MtUser> shareMembers = new ArrayList<MtUser>();
	List<MtUser> userList = new ArrayList<MtUser>();
	
	List<TrMemoShareMember> memoShareMembers = new ArrayList<TrMemoShareMember>();
	
	@ModelAttribute(value = "formModel")
	public TrMemo setUpTrMemo() {
		return new TrMemo();
	}
	
	@RequestMapping(value = "/memo",method=RequestMethod.GET,params="Top")
	public String saveComplete(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		shareMembers = new ArrayList<MtUser>();
		memoShareMembers = new ArrayList<TrMemoShareMember>();
		userList = new ArrayList<MtUser>();
		return "redirect:/memoTop";
	}
	
	@RequestMapping(value = "/memoTop",method=RequestMethod.GET)
	public ModelAndView memoList(ModelAndView mav,@AuthenticationPrincipal UserDetails userDetail) {
		this.userDetail=userDetail;
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		mav.addObject("userCode", user.getUserCode());
		//TrMemo memo = new TrMemo();
		//mav.addObject("formModel", memo);
		mav.addObject("memoList", memoRepository.getMemoList(user.getUserCode()));
		mav.addObject("userList", userRepository.getUserListForMemo(user.getUserCode()));
		mav.setViewName("/400memo/410memoTop");
		return mav;
	}
	
	@RequestMapping(value = "/memoCre",method=RequestMethod.POST)
	public ModelAndView memoCre(ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		TrMemo memo = new TrMemo();
		mav.addObject("formModel", memo);
		mav.addObject("userList", userRepository.getUserListForMemo(user.getUserCode()));
		mav.setViewName("/400memo/411memoCre");
		return mav;
	}
	
	@RequestMapping(value = "/memoCreConf",method=RequestMethod.GET)
	public ModelAndView memoCreConf(
			@ModelAttribute("formModel")TrMemo memo,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			String[] inputMultiCheckRes = memo.getInputMultiCheck();
			if(inputMultiCheckRes!=null) {
				for(String s:inputMultiCheckRes) {
					shareMembers.add(userRepository.findByUserCode(s).get());
				}
			}
			mav.addObject("shareMembers", shareMembers);
			mav.setViewName("/400memo/412memoCreConf");
		}else{
			mav.addObject("msg", "エラーが発生しました");
			mav.addObject("userList", userRepository.getUserListForMemo(user.getUserCode()));
			mav.setViewName("/400memo/411memoCre");
		}
		return mav;
	}
	
	@RequestMapping(value = "/memoRegExe",method=RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView MemoRegExe(
			@ModelAttribute("formModel")TrMemo memo,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		memo.setMtUser(user);
		memo.setEditDate(new Date());
		memo.setValidity(true);
		memo.setParentMemoId(0L);//返信や更新のときはべつにしないとまずい
		for(MtUser u:shareMembers) {
			memo.addTrMemoShareMember(new TrMemoShareMember(u));
		}	
		memoRepository.saveAndFlush(memo);
		memo.setInputMultiCheck(null);
		mav.setViewName("redirect:/memo?Top");
		return mav;
	}
	
	@RequestMapping(value = "/memoChg/{memoId}",method=RequestMethod.GET)
	public ModelAndView MemoChg(
			@PathVariable String memoId,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		userList = userRepository.getUserListForMemo(user.getUserCode());
		TrMemo memo = memoRepository.findByMemoId(Long.parseLong(memoId)).get();
		memoShareMembers=memo.getTrMemoShareMembers();
		for(MtUser u:userList) {
			u.setMemoShare(false);
			for(TrMemoShareMember m:memoShareMembers) {
				if(u.getUserCode().equals(m.getMtUser().getUserCode())) {
					u.setMemoShare(true);
					break;
				}
			}
		}
		mav.addObject("userList", userList);
		mav.addObject("formModel", memo);
		mav.setViewName("/400memo/421memoChg");
		return mav;
	}
	
	@RequestMapping(value = "/memoChgConf",method=RequestMethod.GET)
	public ModelAndView MemoChgConf(
			@ModelAttribute("formModel")TrMemo memo,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			String[] inputMultiCheckRes = memo.getInputMultiCheck();
			if(inputMultiCheckRes!=null) {
				for(String s:inputMultiCheckRes) {
					shareMembers.add(userRepository.findByUserCode(s).get());
				}
			}
			mav.addObject("shareMembers", shareMembers);
			mav.setViewName("/400memo/422memoChgConf");
		}else{
			mav.addObject("msg", "エラーが発生しました");
			mav.addObject("userList", userRepository.getUserListForMemo(user.getUserCode()));
			mav.setViewName("/400memo/421memoChg");
		}
		return mav;
	}
	
	@RequestMapping(value = "/memoChgExe",method=RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView MemoChgExe(
			@ModelAttribute("formModel")TrMemo memo,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		memo.setMtUser(user);
		memo.setEditDate(new Date());
		memo.setValidity(true);
		
		if(shareMembers.size()==0) {
			for(int i = 0;i<memoShareMembers.size();i++) {
				memo.removeTrMemoShareMember(memoShareMembers.get(i));
			}
		}else {		
			for(int i = 0;i<memoShareMembers.size();i++) {
				int j=0;
				for(;j<shareMembers.size();j++) {
					if(memoShareMembers.get(i).getMtUser().getUserCode().equals(shareMembers.get(j).getUserCode())) {
						break;
					}
				}
				if(j==shareMembers.size()) {
					memo.removeTrMemoShareMember(memoShareMembers.get(i));
					memoShareMembers.remove(i);
				}
			}
		
			for(MtUser u:shareMembers) {
				int i=0;
				for(;i<memoShareMembers.size();i++) {
					if(u.getUserCode().equals(memoShareMembers.get(i).getMtUser().getUserCode())) {
						break;
					}
				}
				if(i==memoShareMembers.size()) {
					memo.addTrMemoShareMember(new TrMemoShareMember(u));
				}
			}	
		}
		memoRepository.saveAndFlush(memo);
		memo.setInputMultiCheck(null);
		mav.setViewName("redirect:/memo?Top");
		return mav;
	}
	
	@RequestMapping(value = "/memoRep/{memoId}",method=RequestMethod.GET)
	public ModelAndView MemoRep(
			@PathVariable String memoId,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		userList = userRepository.getUserListForMemo(user.getUserCode());
		TrMemo parentMemo = memoRepository.findByMemoId(Long.parseLong(memoId)).get();
		memoShareMembers=parentMemo.getTrMemoShareMembers();
		for(MtUser u:userList) {
			u.setMemoShare(false);
			if(u.getUserCode().equals(parentMemo.getMtUser().getUserCode())) {
				u.setMemoShare(true);
			}else {
				for(TrMemoShareMember m:memoShareMembers) {
					if(u.getUserCode().equals(m.getMtUser().getUserCode())) {
						u.setMemoShare(true);
						break;
					}
				}
			}
		}
		TrMemo repMemo = new TrMemo();
		repMemo.setParentMemoId(parentMemo.getMemoId());
		repMemo.setTitle("Re:"+parentMemo.getTitle());
		mav.addObject("userList", userList);
		mav.addObject("formModel", repMemo);
		mav.setViewName("/400memo/431memoRep");
		return mav;
	}
	
	@RequestMapping(value = "/memoRepConf",method=RequestMethod.GET)
	public ModelAndView memoRepConf(
			@ModelAttribute("formModel")TrMemo repMemo,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			String[] inputMultiCheckRes = repMemo.getInputMultiCheck();
			if(inputMultiCheckRes!=null) {
				for(String s:inputMultiCheckRes) {
					shareMembers.add(userRepository.findByUserCode(s).get());
				}
			}
			mav.addObject("shareMembers", shareMembers);
			mav.setViewName("/400memo/432memoRepConf");
		}else{
			mav.addObject("msg", "エラーが発生しました");
			mav.addObject("userList", userRepository.getUserListForMemo(user.getUserCode()));
			mav.setViewName("/400memo/431memoRep");
		}
		return mav;
	}
	
	@RequestMapping(value = "/memoRepExe",method=RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView MemoRepExe(
			@ModelAttribute("formModel")TrMemo repMemo,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		repMemo.setMtUser(user);
		repMemo.setEditDate(new Date());
		repMemo.setValidity(true);
		for(MtUser u:shareMembers) {
			repMemo.addTrMemoShareMember(new TrMemoShareMember(u));
		}	
		memoRepository.saveAndFlush(repMemo);
		repMemo.setInputMultiCheck(null);
		mav.setViewName("redirect:/memo?Top");
		return mav;
	}
}