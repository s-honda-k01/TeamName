package com.pci.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pci.entity.MtRole;

public interface RoleRepository extends JpaRepository<MtRole, String> {
	public MtRole findByRoleName(String roleName);
}
