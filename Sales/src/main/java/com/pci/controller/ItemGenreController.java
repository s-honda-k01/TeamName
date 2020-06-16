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

import com.pci.entity.MtItemGenre;
import com.pci.entity.MtUser;
import com.pci.repository.ItemGenreRepository;
import com.pci.repository.UserRepository;

@Controller
@SessionAttributes("formModel")
public class ItemGenreController {
	UserDetails userDetail;
	MtUser user;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ItemGenreRepository itemGenreRepository;
	
	@ModelAttribute(value = "formModel")
	public MtItemGenre setUpMtItemGenre() {
		return new MtItemGenre();
	}
	@RequestMapping(value = "/itemGenre",method=RequestMethod.GET,params = "list")
	public String saveComplete(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/itemGenreList";
	}

	@RequestMapping(value = "/itemGenreList",method=RequestMethod.GET)
	public ModelAndView itemGenreList(ModelAndView mav,@AuthenticationPrincipal UserDetails userDetail) {
		this.userDetail=userDetail;
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		mav.addObject("itemGenreList", itemGenreRepository.findAll());
		mav.setViewName("/200manager/240itemGenreList");
		return mav;
	}
	
	@RequestMapping(value = "/itemGenreUpd/{itemGenreCode}",method=RequestMethod.GET)
	public ModelAndView itemGenreUpd(
			@PathVariable String itemGenreCode,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		mav.addObject("formModel", itemGenreRepository.findByItemGenreCode(itemGenreCode).get());
		mav.setViewName("/200manager/245itemGenreUpd");
		return mav;
	}
	
	@RequestMapping(value = "/itemGenreUpdConf",method=RequestMethod.POST)
	public ModelAndView itemGenreUpdConf(
			@ModelAttribute("formModel")MtItemGenre mtItemGenre,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			mav.setViewName("/200manager/246itemGenreUpdConf");
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.setViewName("/200manager/245itemGenreUpd");
		}
		return mav;
	}
	
	@RequestMapping(value = "/itemGenreCre",method = RequestMethod.POST)
	public ModelAndView itemGenreCre(ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		MtItemGenre itemGenre = new MtItemGenre();
		mav.addObject("formModel", itemGenre);
		mav.setViewName("/200manager/241itemGenreCre");
		return mav;
	}
	
	@RequestMapping(value = "/itemGenreCreConf",method=RequestMethod.POST)
	public ModelAndView itemGenreCreConf(
			@ModelAttribute("formModel")MtItemGenre mtItemGenre,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			if(itemGenreRepository.findByItemGenreCode(mtItemGenre.getItemGenreCode()).isEmpty()) {
				mav.setViewName("/200manager/242itemGenreCreConf");				
			}else {
				mav.addObject("msg", "その商品区分コードはすでに使われています");
				mav.setViewName("/200manager/241itemGenreCre");
			}
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.setViewName("/200manager/241itemGenreCre");
		}
		return mav;
	}
	
	@RequestMapping(value = "/itemGenreRegExe",method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView itemGenreRegExe(
			@ModelAttribute("formModel")MtItemGenre mtItemGenre,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		itemGenreRepository.saveAndFlush(mtItemGenre);
		mav.setViewName("redirect:/itemGenre?list");
		return mav;
	}
}
