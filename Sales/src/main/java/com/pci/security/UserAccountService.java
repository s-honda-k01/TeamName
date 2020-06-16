package com.pci.security;

import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pci.entity.MtUser;
import com.pci.repository.RoleRepository;
import com.pci.repository.UserRepository;

@Service
public class UserAccountService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String usercode) throws UsernameNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		if(usercode == null || "".equals(usercode)) {
			throw new UsernameNotFoundException("usercode is empty");
		}
		Optional<MtUser> exist = userRepository.findByUserCode(usercode);
		if(exist==null) {
			throw new UsernameNotFoundException("user is not found "+ usercode);
		}
		MtUser user = exist.get();
		return new UserAccount(user,getAuthorities(user));
	}
	
	private Collection<GrantedAuthority> getAuthorities(MtUser user){
		if(user.getMtRole().getRoleName().equals("admin")) {
			return AuthorityUtils.createAuthorityList("ROLE_ADMIN");
		}else if(user.getMtRole().getRoleName().equals("manager")) {
			return AuthorityUtils.createAuthorityList("ROLE_MANAGER");
		}else {
			return AuthorityUtils.createAuthorityList("ROLE_STAFF");
		}
	}
	
	@Transactional
	public void registerAdmin(String userCode,String username,String password,boolean validity) {
		userRepository.save(new MtUser(userCode,username,passwordEncoder.encode(password),roleRepository.findByRoleCode("1"),validity));
	}
	@Transactional
	public void registerManager(String userCode,String username,String password,boolean validity) {
		userRepository.save(new MtUser(userCode,username,passwordEncoder.encode(password),roleRepository.findByRoleCode("2"),validity));
	}
	@Transactional
	public void registerStaff(String userCode,String username,String password,boolean validity) {
		userRepository.save(new MtUser(userCode,username,passwordEncoder.encode(password),roleRepository.findByRoleCode("3"),validity));
	}
}
