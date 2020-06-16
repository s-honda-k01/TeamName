package com.pci.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TR_MEMO_SHARE_MEMBER database table.
 * 
 */
@Entity
@Table(name="TR_MEMO_SHARE_MEMBER")
@NamedQuery(name="TrMemoShareMember.findAll", query="SELECT t FROM TrMemoShareMember t")
public class TrMemoShareMember implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="TR_MSM_SHARE_MEMBERID_GENERATOR", sequenceName="TR_MSM_SHARE_MEMBER_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TR_MSM_SHARE_MEMBERID_GENERATOR")
	@Column(name="SHARE_MEMBER_ID")
	private long shareMemberId;

	//bi-directional many-to-one association to MtUser
	@ManyToOne
	@JoinColumn(name="USER_CODE")
	private MtUser mtUser;

	//bi-directional many-to-one association to TrMemo
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="MEMO_ID")
	private TrMemo trMemo;

	public TrMemoShareMember() {
	}

	public TrMemoShareMember(MtUser mtUser) {
		this.mtUser = mtUser;
	}
	
	public TrMemoShareMember(MtUser mtUser, TrMemo trMemo) {
		this.mtUser = mtUser;
		this.trMemo = trMemo;
	}

	public TrMemoShareMember(long shareMemberId, MtUser mtUser, TrMemo trMemo) {
		this.shareMemberId = shareMemberId;
		this.mtUser = mtUser;
		this.trMemo = trMemo;
	}

	public long getShareMemberId() {
		return this.shareMemberId;
	}

	public void setShareMemberId(long shareMemberId) {
		this.shareMemberId = shareMemberId;
	}

	public MtUser getMtUser() {
		return this.mtUser;
	}

	public void setMtUser(MtUser mtUser) {
		this.mtUser = mtUser;
	}

	public TrMemo getTrMemo() {
		return this.trMemo;
	}

	public void setTrMemo(TrMemo trMemo) {
		this.trMemo = trMemo;
	}

}