package com.pci.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the MT_CUSTOMER database table.
 * 
 */
@Entity
@Table(name="MT_CUSTOMER")
@NamedQuery(name="MtCustomer.findAll", query="SELECT m FROM MtCustomer m")
public class MtCustomer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CUSTOMER_CODE")
	private String customerCode;

	@Column(name="CUSTOMER_NAME")
	private String customerName;

	private boolean validity;

	//bi-directional many-to-one association to TrSale
	@OneToMany(mappedBy="mtCustomer")
	private List<TrSale> trSales;

	public MtCustomer() {
	}

	public MtCustomer(String customerCode, String customerName, boolean validity) {
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.validity = validity;
	}

	public String getCustomerCode() {
		return this.customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public boolean getValidity() {
		return this.validity;
	}

	public void setValidity(boolean validity) {
		this.validity = validity;
	}

	public List<TrSale> getTrSales() {
		return this.trSales;
	}

	public void setTrSales(List<TrSale> trSales) {
		this.trSales = trSales;
	}

	public TrSale addTrSale(TrSale trSale) {
		getTrSales().add(trSale);
		trSale.setMtCustomer(this);

		return trSale;
	}

	public TrSale removeTrSale(TrSale trSale) {
		getTrSales().remove(trSale);
		trSale.setMtCustomer(null);

		return trSale;
	}

}