package com.pci.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TR_MEMO database table.
 * 
 */
@Entity
@Table(name="TR_MEMO")
@NamedQuery(name="TrMemo.findAll", query="SELECT t FROM TrMemo t")
public class TrMemo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TR_MEMO_MEMOID_GENERATOR", sequenceName="TR_MEMO_MEMO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TR_MEMO_MEMOID_GENERATOR")
	@Column(name="MEMO_ID")
	private long memoId;

	private String content;

	@Temporal(TemporalType.DATE)
	@Column(name="EDIT_DATE")
	private Date editDate;

	@Column(name="PARENT_MEMO_ID")
	private Long parentMemoId;

	private String title;

	private boolean validity;

	//bi-directional many-to-one association to MtUser
	@ManyToOne
	@JoinColumn(name="USER_CODE")
	private MtUser mtUser;

	//bi-directional many-to-one association to TrMemoShareMember
	@OneToMany(mappedBy="trMemo",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
	private List<TrMemoShareMember> trMemoShareMembers = new ArrayList<TrMemoShareMember>();
	
	@Transient
	private String[] inputMultiCheck;

	public TrMemo() {
	}
	
	public TrMemo(String content, Date editDate, Long parentMemoId, String title, boolean validity, MtUser mtUser,
			List<TrMemoShareMember> trMemoShareMembers) {
		this.content = content;
		this.editDate = editDate;
		this.parentMemoId = parentMemoId;
		this.title = title;
		this.validity = validity;
		this.mtUser = mtUser;
		this.trMemoShareMembers = trMemoShareMembers;
	}

	public TrMemo(long memoId, String content, Date editDate, Long parentMemoId, String title, boolean validity,
			MtUser mtUser, List<TrMemoShareMember> trMemoShareMembers) {
		this.memoId = memoId;
		this.content = content;
		this.editDate = editDate;
		this.parentMemoId = parentMemoId;
		this.title = title;
		this.validity = validity;
		this.mtUser = mtUser;
		this.trMemoShareMembers = trMemoShareMembers;
	}

	public long getMemoId() {
		return this.memoId;
	}

	public void setMemoId(long memoId) {
		this.memoId = memoId;
	}

	public String getContent() {
		if(content!=null && !content.isEmpty()) {
			return content.replaceAll("\n", "<br/>");
		}else {
			return this.content;
		}
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getEditDate() {
		return this.editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public Long getParentMemoId() {
		return this.parentMemoId;
	}

	public void setParentMemoId(Long parentMemoId) {
		this.parentMemoId = parentMemoId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean getValidity() {
		return this.validity;
	}

	public void setValidity(boolean validity) {
		this.validity = validity;
	}

	public MtUser getMtUser() {
		return this.mtUser;
	}

	public void setMtUser(MtUser mtUser) {
		this.mtUser = mtUser;
	}

	public List<TrMemoShareMember> getTrMemoShareMembers() {
		return this.trMemoShareMembers;
	}

	public void setTrMemoShareMembers(List<TrMemoShareMember> trMemoShareMembers) {
		this.trMemoShareMembers = trMemoShareMembers;
	}

	public TrMemoShareMember addTrMemoShareMember(TrMemoShareMember trMemoShareMember) {
		getTrMemoShareMembers().add(trMemoShareMember);
		trMemoShareMember.setTrMemo(this);

		return trMemoShareMember;
	}

	public TrMemoShareMember removeTrMemoShareMember(TrMemoShareMember trMemoShareMember) {
		getTrMemoShareMembers().remove(trMemoShareMember);
		trMemoShareMember.setTrMemo(null);

		return trMemoShareMember;
	}
	
	public String[] getInputMultiCheck() {
		return inputMultiCheck;
	}
	
	public void setInputMultiCheck(String[] inputMultiCheck) {
		this.inputMultiCheck=inputMultiCheck;
	}

}