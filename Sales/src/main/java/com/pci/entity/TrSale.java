package com.pci.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;


/**
 * The persistent class for the TR_SALES database table.
 * 
 */
@Entity
@Table(name="TR_SALES")
@NamedQuery(name="TrSale.findAll", query="SELECT t FROM TrSale t")
public class TrSale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TR_SALES_SALESID_GENERATOR", sequenceName="TR_SALES_SALES_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TR_SALES_SALESID_GENERATOR")
	@Column(name="SALES_ID")
	private long salesId;

	@NotNull
	private int quantity;

	@Temporal(TemporalType.DATE)
	@Column(name="SALES_DATE")
	@NotNull
	private Date salesDate;

	//bi-directional many-to-one association to MtCustomer
	@ManyToOne
	@JoinColumn(name="CUSTOMER_CODE")
	private MtCustomer mtCustomer;

	//bi-directional many-to-one association to MtItem
	@ManyToOne
	@JoinColumn(name="ITEM_CODE")
	private MtItem mtItem;

	//bi-directional many-to-one association to MtUser
	@ManyToOne
	@JoinColumn(name="USER_CODE")
	private MtUser mtUser;

	@Transient
	@NotNull
	private String salesDateString;
	
	public TrSale() {
	}

	public TrSale(int quantity, MtCustomer mtCustomer, MtItem mtItem, MtUser mtUser) {
		this.quantity = quantity;
		this.mtCustomer = mtCustomer;
		this.mtItem = mtItem;
		this.mtUser = mtUser;
	}

	public TrSale(int quantity, Date salesDate, MtCustomer mtCustomer, MtItem mtItem, MtUser mtUser) {
		this.quantity = quantity;
		this.salesDate = salesDate;
		this.mtCustomer = mtCustomer;
		this.mtItem = mtItem;
		this.mtUser = mtUser;
	}

	public TrSale(long salesId, int quantity, Date salesDate, MtCustomer mtCustomer, MtItem mtItem, MtUser mtUser) {
		this.salesId = salesId;
		this.quantity = quantity;
		this.salesDate = salesDate;
		this.mtCustomer = mtCustomer;
		this.mtItem = mtItem;
		this.mtUser = mtUser;
	}

	public long getSalesId() {
		return this.salesId;
	}

	public void setSalesId(long salesId) {
		this.salesId = salesId;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getSalesDate() {
		return this.salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public MtCustomer getMtCustomer() {
		return this.mtCustomer;
	}

	public void setMtCustomer(MtCustomer mtCustomer) {
		this.mtCustomer = mtCustomer;
	}

	public MtItem getMtItem() {
		return this.mtItem;
	}

	public void setMtItem(MtItem mtItem) {
		this.mtItem = mtItem;
	}

	public MtUser getMtUser() {
		return this.mtUser;
	}

	public void setMtUser(MtUser mtUser) {
		this.mtUser = mtUser;
	}

	public String getSalesDateString() {
		return salesDateString;
	}

	public void setSalesDateString(String salesDateString) {
		this.salesDateString = salesDateString;
	}

}