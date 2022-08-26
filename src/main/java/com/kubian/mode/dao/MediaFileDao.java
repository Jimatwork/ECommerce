package com.kubian.mode.dao;

import com.kubian.mode.MediaFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MediaFileDao extends CrudRepository<MediaFile, Integer> {

    List<MediaFile> findByPidAndType(Long pid,Integer type);

}
