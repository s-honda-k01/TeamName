package com.pci.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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
import com.pci.entity.TrSale;
import com.pci.repository.CustomerRepository;
import com.pci.repository.ItemRepository;
import com.pci.repository.SaleRepository;
import com.pci.repository.UserRepository;

@Controller
@SessionAttributes("formModel")
public class SaleController {
	
	UserDetails userDetail;
	MtUser user;
	@Autowired
	UserRepository userRepository;	
	@Autowired
	SaleRepository saleRepository;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	CustomerRepository customerRepository;

	@ModelAttribute(value = "formModel")
	public TrSale setUpTrSale() {
		return new TrSale();
	}
	
	@RequestMapping(value = "/sales",method=RequestMethod.GET,params="list")
	public String saveComplete(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/salesList";
	}
	
	@RequestMapping(value = "/salesList",method=RequestMethod.GET)
	public ModelAndView salesList(ModelAndView mav,@AuthenticationPrincipal UserDetails userDetail) {
		this.userDetail=userDetail;
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		Collection<? extends GrantedAuthority> auths = userDetail.getAuthorities();
		for(GrantedAuthority ga:auths) {
			if(ga.getAuthority().equals("ROLE_MANAGER")) {
				mav.addObject("salesList", saleRepository.findAll());
				mav.setViewName("/200manager/210salesList");
				break;
			}else{
				mav.addObject("salesList", saleRepository.findByMtUser(user.getUserCode()));
				TrSale sale = new TrSale();
				mav.addObject("formModel", sale);
				mav.addObject("itemList", itemRepository.findAll());
				mav.addObject("customerList", customerRepository.findAll());
				mav.setViewName("/300staff/310salesList");
				break;
			}
		}
		return mav;
	}
	
/*	@RequestMapping(value = "/manager/salesList",method=RequestMethod.GET)
	public ModelAndView salesListOfManager(ModelAndView mav,@AuthenticationPrincipal UserDetails userDetail) {
		this.userDetail=userDetail;
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		mav.addObject("salesList", saleRepository.findAll());
		mav.setViewName("/200manager/210salesList.html");
		return mav;
	}

	@RequestMapping(value = "/staff/salesList",method=RequestMethod.GET)
	public ModelAndView salesListOfStaff(ModelAndView mav,@AuthenticationPrincipal UserDetails userDetail) {
		this.userDetail=userDetail;
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		//mav.addObject("salesList", saleRepository.findByMtUser(userDetail.getUsername()));
		mav.addObject("salesList", saleRepository.findByMtUser(user.getUserCode()));
		mav.setViewName("/300staff/310salesList");
		return mav;
	}
	
	@RequestMapping(value = "/saleCre",method = RequestMethod.POST)
	public ModelAndView SaleCre(ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		TrSale sale = new TrSale();
		mav.addObject("formModel", sale);
		mav.addObject("itemList", itemRepository.findAll());
		mav.addObject("customerList", customerRepository.findAll());
		mav.setViewName("/300staff/311salesCre");
		return mav;
	}
*/	

	@RequestMapping(value = "/saleCreConf",method=RequestMethod.POST)
	public ModelAndView SaleCreConf(
			@ModelAttribute("formModel")TrSale sale,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			mav.setViewName("/300staff/311salesCreConf");
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.addObject("itemList", itemRepository.findAll());
			mav.addObject("customerList", customerRepository.findAll());
			mav.setViewName("/300staff/310salesList");
		}
		return mav;
	}
	
	@RequestMapping(value = "/saleRegExe",method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView SaleRegExe(
			@ModelAttribute("formModel")TrSale sale,
			ModelAndView mav){
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		sale.setMtUser(user);
		sale.setSalesDate(java.sql.Date.valueOf(sale.getSalesDateString()));
		saleRepository.saveAndFlush(sale);
		mav.setViewName("redirect:/sales?list");
		return mav;
	}
	
	@RequestMapping(value = "/saleUpd/{salesId}",method=RequestMethod.GET)
	public ModelAndView SaleUpd(
			@PathVariable String salesId,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		mav.addObject("itemList", itemRepository.findAll());
		mav.addObject("customerList", customerRepository.findAll());
		mav.addObject("formModel", saleRepository.findBySalesId(Long.parseLong(salesId)).get());
		mav.setViewName("/300staff/315salesUpd");
		return mav;
	}
	
	@RequestMapping(value = "/saleUpdConf",method=RequestMethod.POST)
	public ModelAndView SaleUpdConf(
			@ModelAttribute("formModel")TrSale sale,
			BindingResult result,
			ModelAndView mav) {
		user=userRepository.findByUserCode(userDetail.getUsername()).get();
		mav.addObject("userName", user.getUserName());
		if(!result.hasErrors()) {
			mav.setViewName("/300staff/316salesUpdConf");
		}else {
			mav.addObject("msg", "エラーが発生しました");
			mav.addObject("itemList", itemRepository.findAll());
			mav.addObject("customerList", customerRepository.findAll());
			mav.setViewName("/300staff/315salesUpd");
		}
		return mav;
	}
}
