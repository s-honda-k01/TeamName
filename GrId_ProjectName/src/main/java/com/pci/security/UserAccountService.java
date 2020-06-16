package com.pci.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pci.entity.MtUser;
import com.pci.repository.RoleRepository;
import com.pci.repository.UserRepository;


/**
 * UserDetailsService実装クラス
 */
@Service
public class UserAccountService implements UserDetailsService {
	
	@Autowired
    private RoleRepository roleRepo;
	@Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

	/**
	 * ログインチェックを行うメソッド
	 */
	@Override
	public UserDetails loadUserByUsername(String usercode) throws UsernameNotFoundException {
		if (usercode == null || "".equals(usercode)) {
            throw new UsernameNotFoundException("Usercode is empty");
        }
       
        MtUser ac = userRepo.findByUserCode(usercode);
        if (ac == null) {
            throw new UsernameNotFoundException("User not found: " + usercode);
        }
        
        if (!ac.isEnabled()) {
            throw new UsernameNotFoundException("User not found: " + usercode);
        }
        
        UserAccount user = new UserAccount(ac, getAuthorities(ac));

        return user;
	}
	
	/**
	 * ロール情報のリストを返すメソッド
	 * @param account
	 * @return
	 */
	private Collection<GrantedAuthority> getAuthorities(MtUser account){
		
		if(account.getMtRole().getRoleName().equals("admin")){
			return AuthorityUtils.createAuthorityList("ROLE_ADMIN");
		}else if(account.getMtRole().getRoleName().equals("mgr")) {
			return AuthorityUtils.createAuthorityList("ROLE_MGR");
		}else{
			return AuthorityUtils.createAuthorityList("ROLE_USER");
		}		
	}
	
	/**
	 * 管理者ロールのユーザをDBに登録するメソッド
	 * @param username
	 * @param password
	 */
	@Transactional
    public void registerAdmin(String usercode, String password, String username, boolean enabled) {
        MtUser user = new MtUser(usercode, passwordEncoder.encode(password),
        		roleRepo.findByRoleName("admin"), username,  enabled);
        userRepo.save(user);
    }
	
	/**
	 * 管理者ロールのユーザを変更するメソッド
	 * @param username
	 * @param password
	 */
	@Transactional
    public void changeAdmin(String usercode, String password, String username, String name, boolean enabled) {
        MtUser user = new MtUser(usercode, passwordEncoder.encode(password),
        		roleRepo.findByRoleName("admin"), username, enabled);
        userRepo.save(user);
    }

    /**
     * 従業員ロールのユーザーを登録するメソッド
     * @param username
     * @param password
     */
    @Transactional
    public void registerMgr(String usercode, String password, String username, boolean enabled) {
        MtUser user = new MtUser(usercode, passwordEncoder.encode(password),
        		roleRepo.findByRoleName("mgr"),username, enabled);
        userRepo.save(user);
    }

    /**
     * 従業員ロールのユーザーを変更するメソッド
     * @param username
     * @param password
     */
    @Transactional
    public void changeMgr(String usercode, String password, String username, boolean enabled) {
        MtUser user = new MtUser(usercode, passwordEncoder.encode(password),
        		roleRepo.findByRoleName("mgr"), username, enabled);
        userRepo.save(user);
    }

    /**
     * ユーザーロールのユーザーを登録するメソッド
     * @param username
     * @param password
     */
    @Transactional
    public void registerUser(String usercode, String password, String username, boolean enabled) {
        MtUser user = new MtUser(usercode, passwordEncoder.encode(password),
        		roleRepo.findByRoleName("user"), username, enabled);
        userRepo.save(user);
    }

    /**
     * ユーザーロールのユーザーを変更するメソッド
     * @param username
     * @param password
     */
    @Transactional
    public void changeUser(String usercode, String password, String username, boolean enabled) {
        MtUser user = new MtUser(usercode, passwordEncoder.encode(password),
        		roleRepo.findByRoleName("user"), username, enabled);
        userRepo.save(user);
    }
}
