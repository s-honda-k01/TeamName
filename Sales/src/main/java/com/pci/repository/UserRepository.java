package com.pci.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pci.entity.MtUser;

@Repository
public interface UserRepository extends JpaRepository<MtUser, String> {
	public Optional<MtUser> findByUserCode(String userCode);
	
	public MtUser findByUserName(String username);
	
	@Query("select m from MtUser m where m.mtRole.roleName = 'manager'")
	public List<MtUser> getManagerList();
	
	@Query("select m from MtUser m where m.mtRole.roleName != 'admin' and m.validity != 0 and m.userCode != :userCode")
	public List<MtUser> getUserListForMemo(@Param("userCode") String userCode);
}
