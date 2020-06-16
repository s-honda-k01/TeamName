package com.pci.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.pci.entity.MtUser;
import com.pci.repository.UserRepository;
import com.pci.security.UserAccount;

/**
 * ログインコントローラー
 */
@SessionAttributes("loginUser")
@Controller
public class LoginController {
	@Autowired
	UserRepository userRepo;
	
	@ModelAttribute(value = "loginUser")
	public MtUser setupLoginUser() {
		return new MtUser();
	}
	
	/**
	 * /loginで起動されるメソッド
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView mav) {
		mav.addObject("iserror", false);
		mav.setViewName("000login");
	    return mav;
	}

	/**
	 * /indexで起動されるメソッド
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav, Authentication authentication) {
		mav.addObject("iserror", false);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		// 本来であればSpring Securityでは、あるユーザに複数のロールを割り当てられる仕様だが、
		// ここでは単一ユーザに単一のロールとしている。
		
		authentication = SecurityContextHolder.getContext().getAuthentication();
	    if(authentication.getPrincipal() instanceof UserAccount){
	    	UserAccount user = UserAccount.class.cast(authentication.getPrincipal());
	    	MtUser loginUser = userRepo.findByUserCode(user.getUserCode());
	    	mav.addObject("loginUser", loginUser);
				for (GrantedAuthority grantedAuthority : authorities){
			        if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
			            mav.setViewName("100admin/100adminTop");
			            break;
			        }else if (grantedAuthority.getAuthority().equals("ROLE_MGR")) {
			        	// 担当生徒リスト取得
			        	mav.setViewName("200mgr/200mgrTop");;
			            break;
			        }else if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
			        	mav.setViewName("300user/300userTop");
			            break;
			        }
			    }
	    }
	    return mav;
	}

	/**
	 * /login-errorで起動されるメソッド
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login-error", method = RequestMethod.GET)
	public ModelAndView loginError(ModelAndView mav) {
		mav.addObject("iserror",true);
		mav.setViewName("000login");
		return mav;
	}
}