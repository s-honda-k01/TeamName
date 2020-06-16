package com.pci.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pci.entity.TrMemo;

@Repository
public interface MemoRepository extends JpaRepository<TrMemo, Long> {
	public Optional<TrMemo> findByMemoId(Long memoId);
	
	@Query("select max(t.memoId) FROM TrMemo t group by t.mtUser.userCode having t.mtUser.userCode = :userCode")
	public Long getLatestMemoId(@Param("userCode") String userCode);
	
	@Query("select t FROM TrMemo t WHERE t.mtUser.userCode= :userCode")
	public List<TrMemo> findByMtUser(@Param("userCode") String userCode);

	@Query("select t from TrMemo t where t.mtUser.userCode = :userCode or t.memoId in ( select s.trMemo.memoId from TrMemoShareMember s where s.mtUser.userCode = :userCode)")
	public List<TrMemo> getMemoList(@Param("userCode") String userCode);
}