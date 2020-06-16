package com.pci.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pci.entity.MtItemGenre;

@Repository
public interface ItemGenreRepository extends JpaRepository<MtItemGenre, String> {
	public Optional<MtItemGenre> findByItemGenreCode(String itemGenreCode);
}
