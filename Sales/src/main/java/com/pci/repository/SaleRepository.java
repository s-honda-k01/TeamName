package com.pci.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pci.entity.TrSale;

@Repository
public interface SaleRepository extends JpaRepository<TrSale, Long> {
	
	@Query("select t FROM TrSale t WHERE t.mtUser.userCode= :userCode")
	public List<TrSale> findByMtUser(@Param("userCode") String userCode);
	
	public Optional<TrSale> findBySalesId(Long salesId);

}
