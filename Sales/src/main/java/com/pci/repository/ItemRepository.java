package com.pci.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pci.entity.MtItem;

@Repository
public interface ItemRepository extends JpaRepository<MtItem, String> {
	public Optional<MtItem> findByItemCode(String itemCode); 

}
