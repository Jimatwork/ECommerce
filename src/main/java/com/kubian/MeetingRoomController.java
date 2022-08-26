/*
 * (C) Copyright 2016 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.kubian;

import java.util.List;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kubian.mode.MeetingRoom;
import com.kubian.mode.dao.MeetingRoomDao;
import com.kubian.mode.util.ReturnMsg;

//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;

@RestController
public class MeetingRoomController {

	private static final Logger log = LoggerFactory.getLogger(MeetingRoomController.class);

	@Autowired
	private MeetingRoomDao meetingRoomDao;
	private static ReturnMsg returnMsg = new ReturnMsg();

	@RequestMapping("/addMeetingRoom")
	public Object addMeetingRoom(MeetingRoom room) {
		MeetingRoom newroom = meetingRoomDao.save(room);
		if (null == newroom) {
			return "新增房间失败";
		} else {
			return "新增房间:" + newroom.getRoomNum();
		}
	}

	@RequestMapping("/delRoom")
	public Object delRoom(@RequestParam("roomid") String roomid) {
		MeetingRoom newroom = meetingRoomDao.findByRoomNum(roomid);
		if (null == newroom) {
			return "删除房间失败";
		} else {
			newroom.setMeetingStatus(2);
			meetingRoomDao.save(newroom);
			return "删除房间成功";
		}
	}

	@RequestMapping("/addMeetingPart")
	public Object addMeetingPart(@RequestParam("roomid") String roomid, @RequestParam("parts") String parts) {
		MeetingRoom newroom = meetingRoomDao.findByRoomNum(roomid);
		if (null == newroom) {
			return "新增参与者失败！";
		} else {
			newroom.setMeetingPart(newroom.getMeetingPart() + "," + parts);
			meetingRoomDao.save(newroom);
			return "新增参与者成功！";
		}
	}

	@RequestMapping("/delMeetingPart")
	public Object delMeetingPart(@RequestParam("roomid") String roomid, @RequestParam("part") String part) {
		MeetingRoom newroom = meetingRoomDao.findByRoomNum(roomid);
		if (null == newroom) {
			return "删除参与者失败！";
		} else {
			String parts = newroom.getMeetingPart().replaceAll(part, "");
			newroom.setMeetingPart(parts);
			meetingRoomDao.save(newroom);
			return "新增参与者成功！";
		}
	}

	@RequestMapping("/getUserRooms")
	public Object getUserRooms(@RequestParam("user") String user) {
		List<MeetingRoom> roomList = meetingRoomDao.findRoomByUserName(user);
		return roomList;
	}

}
