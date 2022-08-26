package com.kubian.mode.dao;

import com.kubian.mode.ProductComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ProductCommentDao extends CrudRepository<ProductComment, Integer> {

    Page<ProductComment> findByPid(Pageable pageable, Long pid);

    ProductComment findById(Long id);

    List<ProductComment> findByPid(Long pid);

    @Query("select count(type) from ProductComment where type= :type and pid=:pid")
    Integer findByTypeAndPid(@Param("pid") Long pid, @Param("type") Integer type);

    @Query("select count(type) from ProductComment where  pid=:pid")
    Integer findByTypeAndPid(@Param("pid") Long pid);
}
