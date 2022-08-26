/*
 * (C) Copyright 2015 Kurento (http://kurento.org/)
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
 */

package com.kubian.rpc;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.kurento.client.MediaPipeline;
import org.kurento.client.MediaProfileSpecType;
import org.kurento.client.RecorderEndpoint;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.jsonrpc.Session;
import org.kurento.jsonrpc.Transaction;
import org.kurento.jsonrpc.message.Request;
import org.kurento.room.NotificationRoomManager;
import org.kurento.room.api.pojo.ParticipantRequest;
import org.kurento.room.api.pojo.UserParticipant;
import org.kurento.room.exception.RoomException;
import org.kurento.room.internal.ProtocolElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.kubian.mode.util.RedisClient;

import redis.clients.jedis.Jedis;

/**
 * Controls the user interactions by delegating her JSON-RPC requests to the
 * room API.
 * 
 * @author Radu Tom Vlad (rvlad@naevatec.com)
 */
public class JsonRpcUserControl {

	private static final Logger log = LoggerFactory
			.getLogger(JsonRpcUserControl.class);

	public static final String RECORDING_PATH = "file:///tmp/";
	public static final String RECORDING_EXT = ".webm";
//	@Autowired
//	private AppointmentTimeDetailDao app;
	
	
	protected NotificationRoomManager roomManager;


	@Autowired
	public JsonRpcUserControl(NotificationRoomManager roomManager) {
		this.roomManager = roomManager;
	}
	
	public void startRecording(Transaction transaction, Request<JsonObject> request,
			ParticipantRequest participantRequest){
	    try{
	    	String pid = participantRequest.getParticipantId();
	    	MediaPipeline mediaPipeline = roomManager.getPipeline(pid);
	    	WebRtcEndpoint webRtcEndpoint  = new WebRtcEndpoint.Builder(mediaPipeline).build();
	    	RecorderEndpoint recorderEndpoint  = new RecorderEndpoint.Builder(mediaPipeline, RECORDING_PATH + "test" + RECORDING_EXT).build();
	    	webRtcEndpoint.connect(recorderEndpoint);
	    	recorderEndpoint.record();
	        log.info("Started recording broadcast-------------------------");
	    }
	    catch(Exception e){
	        log.error("Something bad happended: + " + e.getMessage());
	    }
	}
	

	public void joinRoom(Transaction transaction, Request<JsonObject> request,
			ParticipantRequest participantRequest) throws IOException,
			InterruptedException, ExecutionException {
		String roomName = getStringParam(request,
				ProtocolElements.JOINROOM_ROOM_PARAM);
		String userName = getStringParam(request,
				ProtocolElements.JOINROOM_USER_PARAM);

		boolean dataChannels = false;
		if (request.getParams().has(
				ProtocolElements.JOINROOM_DATACHANNELS_PARAM)) {
			dataChannels = request.getParams()
					.get(ProtocolElements.JOINROOM_DATACHANNELS_PARAM)
					.getAsBoolean();
		}

		ParticipantSession participantSession = getParticipantSession(transaction);
		participantSession.setParticipantName(userName);
		participantSession.setRoomName(roomName);
		participantSession.setDataChannels(dataChannels);
		roomManager.joinRoom(userName, roomName, dataChannels, true,
				participantRequest);
	}

	public void publishVideo(Transaction transaction,
			Request<JsonObject> request, ParticipantRequest participantRequest) {
		String sdpOffer = getStringParam(request,
				ProtocolElements.PUBLISHVIDEO_SDPOFFER_PARAM);
		boolean doLoopback = getBooleanParam(request,
				ProtocolElements.PUBLISHVIDEO_DOLOOPBACK_PARAM);
		roomManager.publishMedia(participantRequest, sdpOffer, doLoopback);
		//startRecording(transaction, request, participantRequest);
	}

	public void unpublishVideo(Transaction transaction,
			Request<JsonObject> request, ParticipantRequest participantRequest) {
		roomManager.unpublishMedia(participantRequest);
	}

	public void receiveVideoFrom(final Transaction transaction,
			final Request<JsonObject> request,
			ParticipantRequest participantRequest) {

		String senderName = getStringParam(request,
				ProtocolElements.RECEIVEVIDEO_SENDER_PARAM);
		senderName = senderName.substring(0, senderName.indexOf("_"));

		String sdpOffer = getStringParam(request,
				ProtocolElements.RECEIVEVIDEO_SDPOFFER_PARAM);

		roomManager.subscribe(senderName, sdpOffer, participantRequest);
	}

	public void unsubscribeFromVideo(Transaction transaction,
			Request<JsonObject> request, ParticipantRequest participantRequest) {

		String senderName = getStringParam(request,
				ProtocolElements.UNSUBSCRIBEFROMVIDEO_SENDER_PARAM);
		senderName = senderName.substring(0, senderName.indexOf("_"));

		roomManager.unsubscribe(senderName, participantRequest);
	}

	public void leaveRoomAfterConnClosed(String sessionId) {
		try {
			roomManager.evictParticipant(sessionId);
			log.info("Evicted participant with sessionId {}", sessionId);
		} catch (RoomException e) {
			log.warn("Unable to evict: {}", e.getMessage());
			log.trace("Unable to evict user", e);
		}
	}

	public void leaveRoom(Transaction transaction, Request<JsonObject> request,
			ParticipantRequest participantRequest) {
		boolean exists = false;
		String pid = participantRequest.getParticipantId();
		// trying with room info from session
		String roomName = null;
		if (transaction != null) {
			roomName = getParticipantSession(transaction).getRoomName();
		}
		log.info("离开房间--leaveRoom----roomName----"+roomName);
		if (roomName == null) { // null when afterConnectionClosed
			log.warn(
					"No room information found for participant with session Id {}. "
							+ "Using the admin method to evict the user.", pid);
			leaveRoomAfterConnClosed(pid);
			
		} else {
			Set<UserParticipant> uspt = roomManager.getParticipants(roomName);
			//如果房只有一个人，退出房间后就关闭房间;
			if(uspt.size() == 1){
//				AppointmentTimeDetail apps= app.findByRoomId(roomName);
//				apps.setRoomId(null);
//				apps.setState(3);
//				apps.setVideoEndTime(new Date());
//				app.save(apps);
				Jedis jedis=RedisClient.getJedis();
//				String s= jedis.get(apps.getPatient().getId()+"video");
//				if(s!=null){
////					jedis.expire(apps.getPatient().getId()+"video",0);
//				}
			}
			// sanity check, don't call leaveRoom unless the id checks out
			for (UserParticipant part : uspt) {
				if (part.getParticipantId().equals(
						participantRequest.getParticipantId())) {
					exists = true;
					break;
				}
			}
			if (exists) {
				log.debug("Participant with sessionId {} is leaving room {}",
						pid, roomName);
				roomManager.leaveRoom(participantRequest);
				log.info("Participant with sessionId {} has left room {}", pid,
						roomName);
			} else {
				log.warn(
						"Participant with session Id {} not found in room {}. "
								+ "Using the admin method to evict the user.",
						pid, roomName);
				leaveRoomAfterConnClosed(pid);
			}
			
		}
	}

	public void onIceCandidate(Transaction transaction,
			Request<JsonObject> request, ParticipantRequest participantRequest) {
		String endpointName = getStringParam(request,
				ProtocolElements.ONICECANDIDATE_EPNAME_PARAM);
		String candidate = getStringParam(request,
				ProtocolElements.ONICECANDIDATE_CANDIDATE_PARAM);
		String sdpMid = getStringParam(request,
				ProtocolElements.ONICECANDIDATE_SDPMIDPARAM);
		int sdpMLineIndex = getIntParam(request,
				ProtocolElements.ONICECANDIDATE_SDPMLINEINDEX_PARAM);

		roomManager.onIceCandidate(endpointName, candidate, sdpMLineIndex,
				sdpMid, participantRequest);
	}

	public void sendMessage(Transaction transaction,
			Request<JsonObject> request, ParticipantRequest participantRequest) {
		String userName = getStringParam(request,
				ProtocolElements.SENDMESSAGE_USER_PARAM);
		String roomName = getStringParam(request,
				ProtocolElements.SENDMESSAGE_ROOM_PARAM);
		String message = getStringParam(request,
				ProtocolElements.SENDMESSAGE_MESSAGE_PARAM);

		log.debug("Message from {} in room {}: '{}'", userName, roomName,
				message);

		roomManager
				.sendMessage(message, userName, roomName, participantRequest);
	}
	
	/**
	 * 自定义信令转发（暂时只转发白板信令）
	 * @param transaction
	 * @param request
	 * @param participantRequest
	 * @param notificationService
	 */
	public void customRequest(Transaction transaction,
			Request<JsonObject> request, ParticipantRequest participantRequest,JsonRpcNotificationService notificationService) {
		String roomName = getStringParam(request,"room");
		String eventName = getStringParam(request,"eventName");
		String userName = getStringParam(request,"user");
		log.info("eventName------testprint----------------------"+eventName);
		log.info("customRequest------testprint----------------------"+request.getParams());
		Set<UserParticipant> uspt = roomManager.getParticipants(roomName); 
		
		for (UserParticipant part : uspt) {
			log.info("part.getUserName()-----*************------"+part.getUserName());
			switch (eventName) {
				case "deleteUser" :
					log.info(userName+"==========part.getUserName()------testprint----------------------"+part.getUserName());
					if (part.getUserName().equals(userName)) {
						notificationService.sendNotification(part.getParticipantId(), ProtocolElements.CUSTOMREQUEST_METHOD, request.getParams());
					}
					break;
				default :
					if (!part.getParticipantId().equals(participantRequest.getParticipantId())) {
						notificationService.sendNotification(part.getParticipantId(), ProtocolElements.CUSTOMREQUEST_METHOD, request.getParams());
					}
			        break;
			}
			
		}
	}

	public ParticipantSession getParticipantSession(Transaction transaction) {
		Session session = transaction.getSession();
		ParticipantSession participantSession = (ParticipantSession) session
				.getAttributes().get(ParticipantSession.SESSION_KEY);
		if (participantSession == null) {
			participantSession = new ParticipantSession();
			session.getAttributes().put(ParticipantSession.SESSION_KEY,
					participantSession);
		}
		return participantSession;
	}

	public static String getStringParam(Request<JsonObject> request, String key) {
		if (request.getParams() == null || request.getParams().get(key) == null) {
			throw new RuntimeException("请求传送过来的json参数中没有'" + key
					+ "' 这个key ");
		}
		return request.getParams().get(key).getAsString();
	}

	public static int getIntParam(Request<JsonObject> request, String key) {
		if (request.getParams() == null || request.getParams().get(key) == null) {
			throw new RuntimeException("请求传送过来的json参数中没有'" + key
					+ "' 这个key ");
		}
		return request.getParams().get(key).getAsInt();
	}

	public static boolean getBooleanParam(Request<JsonObject> request,
			String key) {
		if (request.getParams() == null || request.getParams().get(key) == null) {
			throw new RuntimeException("请求传送过来的json参数中没有'" + key
					+ "' 这个key ");
		}
		return request.getParams().get(key).getAsBoolean();
	}
}
