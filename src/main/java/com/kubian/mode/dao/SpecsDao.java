package com.kubian.mode.dao;

import com.kubian.mode.Specs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface SpecsDao extends CrudRepository<Specs, Integer> {

    Specs findById(Long id);

    List<Specs> findByPid(Long pid);

    @Query("select min(price) from Specs where pid=:pid")
    Double findByPidMin(@Param("pid") Long pid);
}
