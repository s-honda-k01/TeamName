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

import com.pci.entity.MtCustomer;
import com.pci.entity.MtUser;
import com.pci.repository.CustomerRepository;
import com.pci.repository.UserRepository;

@Controller
@SessionAttributes("formModel")
public class CustomerController {
	UserDetails userDetail;
	MtUser user;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CustomerRepository customerRepository;
	
	@ModelAttribute(value = "formModel")
	public MtCustomer setUpMtCustomer() {
		return new MtCustomer();
	}
	
	@RequestMapping(value = "/customer",method=RequestMethod.GET,params = "list")
	public String saveComplete(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/customerList";
	}
	
	@RequestMapping(value = "/customerList",method=RequestMethod.GET)
	public ModelAndView customerList(ModelAndView mav,@AuthenticationPrincipal UserDetails userDetail) {
		this.userDetail=userDetail;
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		mav.addObject("customerList", customerRepository.findAll());
		mav.setViewName("/200manager/260customerList");
		return mav;
	}
	@RequestMapping(value = "/customerUpd/{customerCode}",method=RequestMethod.GET)
	public ModelAndView customerUpd(
			@PathVariable String customerCode,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		mav.addObject("formModel", customerRepository.findByCustomerCode(customerCode).get());
		mav.setViewName("/200manager/265customerUpd");
		return mav;
	}
	
	@RequestMapping(value = "/customerUpdConf",method=RequestMethod.POST)
	public ModelAndView customerUpdConf(
			@ModelAttribute("formModel")MtCustomer mtCustomer,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			mav.setViewName("/200manager/266customerUpdConf");
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.setViewName("/200manager/265customerUpd");
		}
		return mav;
	}
	
	@RequestMapping(value = "/customerCre",method = RequestMethod.POST)
	public ModelAndView customerCre(ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		MtCustomer customer = new MtCustomer();
		mav.addObject("formModel", customer);
		mav.setViewName("/200manager/261customerCre");
		return mav;
	}
	
	@RequestMapping(value = "/customerCreConf",method=RequestMethod.POST)
	public ModelAndView customerCreConf(
			@ModelAttribute("formModel")MtCustomer mtCustomer,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			if(customerRepository.findByCustomerCode(mtCustomer.getCustomerCode()).isEmpty()) {
				mav.setViewName("/200manager/262customerCreConf");				
			}else {
				mav.addObject("msg", "その顧客コードはすでに使われています");
				mav.setViewName("/200manager/261customerCre");
			}
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.setViewName("/200manager/261customerCre");
		}
		return mav;
	}
	
	@RequestMapping(value = "/customerRegExe",method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView customerRegExe(
			@ModelAttribute("formModel")MtCustomer mtCustomer,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		customerRepository.saveAndFlush(mtCustomer);
		mav.setViewName("redirect:/customer?list");
		return mav;
	}
}
