package com.kubian.mode.dao;

import org.springframework.data.domain.Pageable; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository; 
import org.springframework.stereotype.Repository; 
import org.springframework.transaction.annotation.Transactional;

import com.kubian.mode.MeetingRoom;

import java.lang.String; 
import java.util.List; 

@Transactional
@Repository
public interface MeetingRoomDao extends CrudRepository<MeetingRoom, Integer>{
	
	@Query("select m from MeetingRoom m where (meetingInit=?1 or meetingPart in ?1) and meetingStatus!=2")
	List<MeetingRoom> findRoomByUserName(String username);
	
	MeetingRoom findByRoomNum(String roomNum);
	
}
