package com.kubian.mode.dao;

import com.kubian.mode.ServerPath;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ServerPathDao extends CrudRepository<ServerPath, Integer> {

	// 得到ServerPath
	List<ServerPath> findAll();

	ServerPath findById(Long id);

	@Query(value = "from ServerPath")
	ServerPath getServerPath();
}
