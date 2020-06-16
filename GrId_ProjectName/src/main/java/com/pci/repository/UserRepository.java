package com.pci.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pci.entity.MtUser;

public interface UserRepository extends JpaRepository<MtUser, String> {
	public MtUser findByUserCode(String usercode);
}
