package com.pci.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * セキュリティ情報設定クラス
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	   
	/**
	 * configureメソッド
	 */
	@Override
	protected void configure(HttpSecurity http)throws Exception{		
		http
			.authorizeRequests()
				.antMatchers("/css/**", "/js/**").permitAll()
				.antMatchers("/100admin/**").hasRole("ADMIN")
				.antMatchers("/200mgr/**").hasRole("MGR")
				.antMatchers("/300user/**").hasRole("USER")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/index", true)
				.failureUrl("/login-error")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login")
				.permitAll();
	}
	
	/**
	 * AuthenticationProviderの設定を行うメソッド
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	void configureAuthenticationManager(AuthenticationManagerBuilder auth)
												throws Exception{		
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}
	
	/**
	 * パスワードのハッシュ化メソッド
	 */
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}