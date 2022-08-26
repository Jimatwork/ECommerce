package com.kubian.mode.dao;

import com.kubian.mode.BusinessClassify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface BusinessClassifyDao extends CrudRepository<BusinessClassify, Integer> {

    BusinessClassify findById(Long id);

    Page<BusinessClassify> findAll(Pageable pageable);

    Page<BusinessClassify> findByState(Pageable pageable, Integer state);
}
