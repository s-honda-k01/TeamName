package com.pci.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.pci.entity.MtUser;
import com.pci.repository.UserRepository;


@Controller
public class LoginController {
	
	@Autowired
	UserRepository userRepository;
	
	UserDetails userDetails;
	MtUser user;
	
	@RequestMapping(value = "/login",method=RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("iserror", false);
		return "000login";
	}
	
	@RequestMapping(value = "/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav,Authentication auth,@AuthenticationPrincipal UserDetails userDetails) {
		mav.addObject("iserror", false);
		this.userDetails=userDetails;
		if(userDetails.isEnabled()) {
			user=userRepository.findByUserCode(userDetails.getUsername()).get();
			mav.addObject("userName", user.getUserName());
			Collection<? extends GrantedAuthority> auths = auth.getAuthorities();
			/*for(GrantedAuthority ga:auths) {
				if(ga.getAuthority().equals("ROLE_ADMIN")) {
					mav.setViewName("forward:/admin/userList");
					break;
				}else if(ga.getAuthority().equals("ROLE_MANAGER")) {
					mav.setViewName("forward:/manager/salesList");
					break;
				}else{
					mav.setViewName("forward:/staff/salesList");
					break;
				}
			}*/
			for(GrantedAuthority ga:auths) {
				if(ga.getAuthority().equals("ROLE_ADMIN")) {
					mav.setViewName("forward:/admin/userList");
					break;
				}else {
					mav.setViewName("forward:/salesList");
					break;
				}
			}
		}else{
			mav.setViewName("/login-error");
		}
		return mav;
	}
	
	@RequestMapping(value = "/login-error",method=RequestMethod.GET)
	public String loginError(Model model) {
		model.addAttribute("iserror", true);
		return "000login";
	}
}
