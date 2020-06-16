package com.pci.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the MT_ITEM_GENRE database table.
 * 
 */
@Entity
@Table(name="MT_ITEM_GENRE")
@NamedQuery(name="MtItemGenre.findAll", query="SELECT m FROM MtItemGenre m")
public class MtItemGenre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ITEM_GENRE_CODE")
	private String itemGenreCode;

	@Column(name="ITEM_GENRE_NAME")
	private String itemGenreName;

	//bi-directional many-to-one association to MtItem
	@OneToMany(mappedBy="mtItemGenre")
	private List<MtItem> mtItems;

	public MtItemGenre() {
	}

	public String getItemGenreCode() {
		return this.itemGenreCode;
	}

	public void setItemGenreCode(String itemGenreCode) {
		this.itemGenreCode = itemGenreCode;
	}

	public String getItemGenreName() {
		return this.itemGenreName;
	}

	public void setItemGenreName(String itemGenreName) {
		this.itemGenreName = itemGenreName;
	}

	public List<MtItem> getMtItems() {
		return this.mtItems;
	}

	public void setMtItems(List<MtItem> mtItems) {
		this.mtItems = mtItems;
	}

	public MtItem addMtItem(MtItem mtItem) {
		getMtItems().add(mtItem);
		mtItem.setMtItemGenre(this);

		return mtItem;
	}

	public MtItem removeMtItem(MtItem mtItem) {
		getMtItems().remove(mtItem);
		mtItem.setMtItemGenre(null);

		return mtItem;
	}

}