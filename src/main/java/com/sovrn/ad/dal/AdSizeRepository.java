package com.sovrn.ad.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sovrn.ad.dal.entity.AdSize;

public interface AdSizeRepository extends JpaRepository<AdSize, Integer>{
	public List<AdSize> findByUsers_Id(int userid);
}
