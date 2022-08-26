package com.kubian.mode.dao;

import com.kubian.mode.RunningTab;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HD
 * @Title: RunningTabDao
 * @ProjectName Idea
 * @Description: TODO
 * @date 2018/9/7/00714:51
 */
@Transactional
@Repository
public interface RunningTabDao extends CrudRepository<RunningTab , Integer> {
    Page<RunningTab> findAll( Pageable pageable);

    Page<RunningTab> findByBid(Long bid , Pageable pageable);

    Page<RunningTab> findByBidAndState(Long bid , Integer state , Pageable pageable);

    Page<RunningTab> findByState(Integer state , Pageable pageable);
}
