package com.kubian.mode;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 会议室
 * @author liugang
 *
 */
@Entity
@Table(name="meeting_room")
public class MeetingRoom implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String meetingName;
	private String roomNum;
	private String meetingDesc;
	//会议发起人
	private String meetingInit;
	//会议参与者
	private String meetingPart;
	private String meetingRecPath;
	private String meetingRtmpPath;
	//会议状态  0：未开始   1：正在进行   2：已结束
	private Integer meetingStatus;
	private Date creationDate;
	private Date startTime;
	
	
	@Id  @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public String getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	public String getMeetingDesc() {
		return meetingDesc;
	}
	public void setMeetingDesc(String meetingDesc) {
		this.meetingDesc = meetingDesc;
	}
	public String getMeetingInit() {
		return meetingInit;
	}
	public void setMeetingInit(String meetingInit) {
		this.meetingInit = meetingInit;
	}
	public String getMeetingPart() {
		return meetingPart;
	}
	public void setMeetingPart(String meetingPart) {
		this.meetingPart = meetingPart;
	}
	public String getMeetingRecPath() {
		return meetingRecPath;
	}
	public void setMeetingRecPath(String meetingRecPath) {
		this.meetingRecPath = meetingRecPath;
	}
	public String getMeetingRtmpPath() {
		return meetingRtmpPath;
	}
	public void setMeetingRtmpPath(String meetingRtmpPath) {
		this.meetingRtmpPath = meetingRtmpPath;
	}
	public Integer getMeetingStatus() {
		return meetingStatus;
	}
	public void setMeetingStatus(Integer meetingStatus) {
		this.meetingStatus = meetingStatus;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	
	
	
}
