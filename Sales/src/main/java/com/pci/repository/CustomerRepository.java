package com.pci.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pci.entity.MtCustomer;

@Repository
public interface CustomerRepository extends JpaRepository<MtCustomer, String> {
	public Optional<MtCustomer> findByCustomerCode(String customerCode);
}
