package com.pci.controller;

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

import com.pci.entity.MtItem;
import com.pci.entity.MtUser;
import com.pci.repository.ItemGenreRepository;
import com.pci.repository.ItemRepository;
import com.pci.repository.UserRepository;

@Controller
@SessionAttributes("formModel")
public class ItemController {
	
	UserDetails userDetail;
	MtUser user;
	@Autowired
	UserRepository userRepository;	
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ItemGenreRepository itemGenreRepository;
	
	@ModelAttribute(value = "formModel")
	public MtItem setUpMtItem() {
		return new MtItem();
	}
	@RequestMapping(value = "/item",method=RequestMethod.GET,params = "list")
	public String saveComplete(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/itemList";
	}
	
	@RequestMapping(value = "/itemList",method=RequestMethod.GET)
	public ModelAndView itemList(ModelAndView mav,@AuthenticationPrincipal UserDetails userDetail) {
		this.userDetail=userDetail;
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		mav.addObject("itemList", itemRepository.findAll());
		mav.setViewName("/200manager/220itemList");
		return mav;
	}
	
	@RequestMapping(value = "/itemUpd/{itemCode}",method=RequestMethod.GET)
	public ModelAndView itemUpd(
			@PathVariable String itemCode,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		mav.addObject("itemGenreList", itemGenreRepository.findAll());
		mav.addObject("formModel", itemRepository.findByItemCode(itemCode).get());
		mav.setViewName("/200manager/225itemUpd");
		return mav;
	}
	
	@RequestMapping(value = "/itemUpdConf",method=RequestMethod.POST)
	public ModelAndView itemUpdConf(
			@ModelAttribute("formModel")MtItem mtItem,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			mav.setViewName("/200manager/226itemUpdConf");
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.addObject("itemGenreList", itemGenreRepository.findAll());
			mav.setViewName("/200manager/225itemUpd");
		}
		return mav;
	}
	
	@RequestMapping(value = "/itemCre",method = RequestMethod.POST)
	public ModelAndView itemCre(ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		MtItem item = new MtItem();
		mav.addObject("formModel", item);
		mav.addObject("itemGenreList", itemGenreRepository.findAll());
		mav.setViewName("/200manager/221itemCre");
		return mav;
	}
	
	@RequestMapping(value = "/itemCreConf",method=RequestMethod.POST)
	public ModelAndView itemCreConf(
			@ModelAttribute("formModel")MtItem mtItem,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			if(itemRepository.findByItemCode(mtItem.getItemCode()).isEmpty()) {
				mav.setViewName("/200manager/222itemCreConf");				
			}else {
				mav.addObject("msg", "その商品コードはすでに使われています");
				mav.addObject("itemGenreList", itemGenreRepository.findAll());
				mav.setViewName("/200manager/221itemCre");
			}
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.addObject("itemGenreList", itemGenreRepository.findAll());
			mav.setViewName("/200manager/221itemCre");
		}
		return mav;
	}
	
	@RequestMapping(value = "/itemRegExe",method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView itemRegExe(
			@ModelAttribute("formModel")MtItem mtItem,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		itemRepository.saveAndFlush(mtItem);
		mav.setViewName("redirect:/item?list");
		return mav;
	}
}
