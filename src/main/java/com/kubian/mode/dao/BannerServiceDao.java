package com.kubian.mode.dao;

import com.kubian.mode.BannerService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Transactional
@Repository
public interface BannerServiceDao extends CrudRepository<BannerService, Integer> {

    BannerService findById(Long id);

    List<BannerService> findByLinkId(Long linkId);

    List<BannerService> findByType(Integer type);

    List<BannerService> findByTypeAndLinkId(Integer type, Long linkId);

}
