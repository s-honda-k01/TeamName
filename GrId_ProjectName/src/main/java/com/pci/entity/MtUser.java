package com.pci.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.validator.constraints.Length;


/**
 * The persistent class for the MT_USER database table.
 * 
 */
@Entity
@Table(name="MT_USER")
@NamedQuery(name="MtUser.findAll", query="SELECT m FROM MtUser m")
public class MtUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_CODE", nullable = false, unique = true, length = 4)
	@Length(min = 1, max = 4)
	private String userCode;

	private boolean enabled;

	@Column(name="USER_NAME")
	@Length(min = 1,max = 15)
	private String userName;

	@Length(min = 1)
	@Column(name="USER_PW")
	private String userPw;

	//bi-directional many-to-one association to MtRole
	@ManyToOne
	@JoinColumn(name="ROLE_CODE")
	private MtRole mtRole;

	public MtUser() {
		this.mtRole = new MtRole();
	}
	public MtUser(String userCode, String userPw, MtRole mtRole, String userName, boolean enabled) {
		super();
		this.userCode = userCode;
		this.userPw = userPw;
		this.userName = userName;
		this.mtRole = mtRole;
		this.enabled = enabled;
	}



	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPw() {
		return this.userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public MtRole getMtRole() {
		return this.mtRole;
	}

	public void setMtRole(MtRole mtRole) {
		this.mtRole = mtRole;
	}

}