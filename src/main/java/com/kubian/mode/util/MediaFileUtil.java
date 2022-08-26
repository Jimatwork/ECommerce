/**
 * 
 */
package com.kubian.mode.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;

/**
 * 媒体文件处理的实用方法。
 * @author liaoz
 */
public class MediaFileUtil {
	//E:\\MediaCoder-0.6.1.4045\\codecs\\mplayer.exe
	//E:\\MediaCoder-0.6.1.4045\\codecs\\ffmpeg.exe
	
	private static String MPLAYER = "/usr/bin/mplayer";
	private static String FFMPEG = "/usr/bin/ffmpeg";
	
	/**
	 * 取MP3播放时间，使用Linux的MPlayer。
	 * @param path 文件路径
	 * @return 秒数
	 * @throws IOException 读文件出错
	 */
	public MediaFileUtil(){
		
	}
	public static MediaFileInfo getMp3FileInfo(String path) throws IOException {
		if (!(new File("/usr")).exists()) {
			MediaFileUtil.MPLAYER = "E:\\MediaCoder\\codecs\\mplayer.exe";
			MediaFileUtil.FFMPEG = "E:\\MediaCoder\\codecs64\\ffmpeg.exe";
		}
		MediaFileInfo info = new MediaFileInfo();
		Process p  = Runtime.getRuntime().exec(MPLAYER  +"  "+path+"  -vo nul -ao nul -identify -frames 0");
		BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		while ((line = is.readLine()) != null) {
			if (line.indexOf("ID_LENGTH") > -1) {
                String s = line.substring(line.indexOf("=") + 1, line.length());
                info.setPlayTime(Double.valueOf(s));
                is.close();
                break;
            }
        }
		p.destroy();
		return info;
	}
	public static MediaFileInfo getAudioFileInfo(String path) throws Exception{
		if (!(new File("/usr")).exists()) {
			MediaFileUtil.MPLAYER = "E:\\MediaCoder\\codecs\\mplayer.exe";
			MediaFileUtil.FFMPEG = "E:\\MediaCoder\\codecs64\\ffmpeg.exe";
		}
		MediaFileInfo file = new MediaFileInfo();
		Process p  = Runtime.getRuntime().exec(MPLAYER  +"  "+path+"  -vo nul -ao nul -identify -frames 0");//获取音频信息
		BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		int count = 0;
		while ((line = is.readLine()) != null) {
			if (line.indexOf("ID_LENGTH") > -1) {
                String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setPlayTime(Double.valueOf(s));
                count++;
            }
			if (line.indexOf("ID_VIDEO_WIDTH") > -1) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setVideoWidth(Double.valueOf(s));
                count++;
            }
			if (line.indexOf("ID_VIDEO_HEIGHT") > -1) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setVideoHeight(Double.valueOf(s));
                count++;
            }
			if (line.indexOf("ID_DEMUXER") > -1) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setDemuxer(s);
                count++;
            }
			if (line.indexOf("ID_AUDIO_CODEC") > -1) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setAudiocodec(s);
                count++;
            }
			if (line.indexOf("ID_VIDEO_FPS") > -1) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
			  	try {
                	file.setFps(Double.valueOf(s));
				} catch (NumberFormatException e) {
					file.setFps(0);
				}
                count++;
            }
			if (count == 6) {
				is.close();
				p.destroy();
				break;
			}
        }
		return file;
		
	}
	//MPLAYER  -identify path -nosound -vc dummy -vo null
	public static MediaFileInfo getVideoFileInfo(String path) throws Exception{
		if (!(new File("/usr")).exists()) {
			
			MediaFileUtil.MPLAYER = "E:\\MediaCoder\\codecs\\mplayer.exe";
			MediaFileUtil.FFMPEG = "E:\\MediaCoder\\codecs64\\ffmpeg.exe";
		}
		MediaFileInfo file = new MediaFileInfo();
		Process p  = Runtime.getRuntime().exec(MPLAYER +"  "+path+"  -vo nul -ao nul -identify -frames 0");//获取音频外的信息
		System.out.println("----123456------"+MPLAYER  +"  "+path+"  -vo nul -ao nul -identify -frames 0");

		BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		int count = 0;
		while ((line = is.readLine()) != null) {
			if (line.indexOf("ID_LENGTH") > -1) {
                String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setPlayTime(Double.valueOf(s));
                count++;
            }
			if (line.indexOf("ID_VIDEO_WIDTH") > -1) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setVideoWidth(Double.valueOf(s));
                count++;
            }
			if (line.indexOf("ID_VIDEO_HEIGHT") > -1) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setVideoHeight(Double.valueOf(s));
                count++;
            }
			if (line.indexOf("ID_DEMUXER") > -1) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setDemuxer(s);
                count++;
            }
			if (line.indexOf("ID_AUDIO_CODEC") > -1) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
                file.setAudiocodec(s);
                System.out.println(" file.setAudiocodec(s)" + file.getAudiocodec());
                count++;
            }
			if (line.indexOf("ID_VIDEO_FPS") > -1 ) {
				String s = line.substring(line.indexOf("=") + 1, line.length());
                try {
                	file.setFps(Double.valueOf(s));
				} catch (NumberFormatException e) {
					file.setFps(0);
				}
                count++;
            }
			if (count == 6) {
				is.close();
				p.destroy();
				break;
			}
        }
		return file;
	}

	public static class MediaFileInfo {
		private double playTime;
		private double videoWidth;
		private double videoHeight;
		private String demuxer;
		private String imgPath;
		private String audiocodec;
		private double fps;

		public double getFps() {
			return fps;
		}
		public void setFps(double fps) {
			this.fps = fps;
		}
		public String getDemuxer() {
			return demuxer;
		}
		public void setDemuxer(String demuxer) {
			this.demuxer = demuxer;
		}
		public double getPlayTime() {
			return playTime;
		}
		public void setPlayTime(double playTime) {
			this.playTime = playTime;
		}
		public double getVideoHeight() {
			return videoHeight;
		}
		public void setVideoHeight(double videoHeight) {
			this.videoHeight = videoHeight;
		}
		public double getVideoWidth() {
			return videoWidth;
		}
		public void setVideoWidth(double videoWidth) {
			this.videoWidth = videoWidth;
		}
		public String getImgPath() {
			return imgPath;
		}
		public void setImgPath(String imgPath) {
			this.imgPath = imgPath;
		}
		public String getAudiocodec() {
			return audiocodec;
		}
		public void setAudiocodec(String audiocodec) {
			this.audiocodec = audiocodec;
		}
	}
}
