package com.pci.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pci.entity.MtUser;

/**
 * UserDetails実装クラス
 */
public class UserAccount implements UserDetails {

	private static final long serialVersionUID = 1L;
	private MtUser user;	
	private Collection<GrantedAuthority> authorities;
	
	/**
	 * コンストラクタ
	 */
	protected UserAccount(){}
	public UserAccount(MtUser account,Collection<GrantedAuthority> authorities){
		this.user = account;
		this.authorities = authorities;		
	}

	/**
	 * 認可(ロール)リストを返却するメソッド
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/**
	 * 登録されているパスワードを返却するメソッド
	 */
	@Override
	public String getPassword() {
		return user.getUserPw();
	}

	/**
	 * ユーザー名を返却するメソッド
	 */
	@Override
	public String getUsername() {
		return user.getUserName();
	}

	/**
	 * アカウントの有効期限を判定するメソッド
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * アカウントがロックされていないかを判定するメソッド
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 資格の有効期限を判定するメソッド
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * ユーザーの有効/無効を判定するメソッド
	 */
	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}
	
	public String getUserCode() {
		return user.getUserCode();
	}

}
