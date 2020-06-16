package com.pci.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the MT_ITEM database table.
 * 
 */
@Entity
@Table(name="MT_ITEM")
@NamedQuery(name="MtItem.findAll", query="SELECT m FROM MtItem m")
public class MtItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITEM_CODE")
	private String itemCode;

	@Column(name="ITEM_NAME")
	private String itemName;

	private int price;

	private String spec;

	private boolean validity;

	//bi-directional many-to-one association to MtItemGenre
	@ManyToOne
	@JoinColumn(name="ITEM_GENRE_CODE")
	private MtItemGenre mtItemGenre;

	//bi-directional many-to-one association to TrSale
	@OneToMany(mappedBy="mtItem")
	private List<TrSale> trSales;

	public MtItem() {
	}

	public MtItem(String itemName, int price, String spec, boolean validity, MtItemGenre mtItemGenre) {
		this.itemName = itemName;
		this.price = price;
		this.spec = spec;
		this.validity = validity;
		this.mtItemGenre = mtItemGenre;
	}
	
	public MtItem(String itemCode, String itemName, int price, String spec, boolean validity, MtItemGenre mtItemGenre) {
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.price = price;
		this.spec = spec;
		this.validity = validity;
		this.mtItemGenre = mtItemGenre;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public boolean getValidity() {
		return this.validity;
	}

	public void setValidity(boolean validity) {
		this.validity = validity;
	}

	public MtItemGenre getMtItemGenre() {
		return this.mtItemGenre;
	}

	public void setMtItemGenre(MtItemGenre mtItemGenre) {
		this.mtItemGenre = mtItemGenre;
	}

	public List<TrSale> getTrSales() {
		return this.trSales;
	}

	public void setTrSales(List<TrSale> trSales) {
		this.trSales = trSales;
	}

	public TrSale addTrSale(TrSale trSale) {
		getTrSales().add(trSale);
		trSale.setMtItem(this);

		return trSale;
	}

	public TrSale removeTrSale(TrSale trSale) {
		getTrSales().remove(trSale);
		trSale.setMtItem(null);

		return trSale;
	}

}