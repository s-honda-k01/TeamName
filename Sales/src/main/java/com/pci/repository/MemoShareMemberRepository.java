package com.pci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pci.entity.TrMemoShareMember;

@Repository
public interface MemoShareMemberRepository extends JpaRepository<TrMemoShareMember, Long> {

}
