package com.kubian.mode.util;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kubian.mode.util.MediaFileUtil.MediaFileInfo;


import java.io.BufferedReader;

public class VideoUtil implements Runnable {
		private String putFile;
		private String outFile;
		private String userImg1;
		private String userImg2;
		private String scale;
		private static String FFMPEG = "/usr/bin/ffmpeg";

		public String getPutFile() {
			return putFile;
		}

		public void setPutFile(String putFile) {
			this.putFile = putFile;
		}

		public String getOutFile() {
			return outFile;
		}

		public void setOutFile(String outFile) {
			this.outFile = outFile;
		}
		// 源视频   输出的视频  左上角水印  右下角水印
		public VideoUtil(String putFile, String outFile,String userImg1,String userImg2) {
			this.putFile = putFile;
			this.outFile = outFile;
			this.userImg1 = userImg1;
			this.userImg2 = userImg2;
			try {
				MediaFileInfo mf = MediaFileUtil.getVideoFileInfo(this.putFile);
				this.scale = mf.getVideoHeight() > mf.getVideoWidth() ? "480x720" : "720x480"; 
				System.out.println(mf.getVideoHeight()+"---------vvvvvv---------" + mf.getVideoWidth());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		/**
		 * 判断播放时间是否超出
		 * @param putFile
		 * @param time
		 * @return
		 */
		public static boolean isTimeOut(String putFile,int time){
			MediaFileInfo mf = null;
			try {
				mf = MediaFileUtil.getVideoFileInfo(putFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(mf.getPlayTime() > time){
				return false;
			}else{
				return true;
			}
		}
		
		// 插入视频的时候截图方法
		public static void pic(String putFile, String picPath) {
			try {
				MediaFileInfo mf = MediaFileUtil.getVideoFileInfo(putFile);
				String scale = mf.getVideoHeight() > mf.getVideoWidth() ? "480x720" : "720x480"; 
				String tempFile = picPath+"_temp"; 
		        // 创建一个List集合来保存从视频中截取图片的命令
		        List<String> cutpic = new ArrayList<String>();
		        if (!(new File("/usr")).exists()) {
		        	cutpic.add("E:\\MediaCoder\\codecs64\\ffmpeg.exe");
				} else {
			        cutpic.add("/root/bin/ffmpeg");
				}
		        cutpic.add("-i");
		        cutpic.add(putFile); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）
		        cutpic.add("-y");
		        cutpic.add("-f");
		        cutpic.add("image2");
		        cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
		        cutpic.add("2"); // 添加起始时间为第2秒
		        cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
		        cutpic.add("0.001"); // 添加持续时间为1毫秒
		        cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
		        cutpic.add(scale); // 添加截取的图片大小为350*240
		        cutpic.add(tempFile); // 添加截取的图片的保存路径
				
		        ProcessBuilder builder = new ProcessBuilder();
		        try {
		            builder.command(cutpic);
		            builder.redirectErrorStream(true);
		            // 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，
		            //因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
		            Process ps1 = builder.start();
		            BufferedReader buffIn1 = new BufferedReader(
			                   new InputStreamReader(
			                           ps1.getInputStream()));
			
					String s1;
					while((s1=buffIn1.readLine())!= null){}
					buffIn1.close();
					if(ps1.waitFor()==0){
						System.out.println("截图完成---------");
						String iconPath = "/usr/local/apache-tomcat-6.0.20/webapps/HanaGlobal/images/kbicon.png";
				        if (!(new File("/usr")).exists()) {
				        	iconPath = tempFile.substring(0,tempFile.indexOf("webapps")+7)+"\\HanaGlobal\\images\\kbicon.png";
						}
						new File(tempFile).delete();
					}else{
						ps1.destroy();
					}
		            
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * 执行cmd命令
		 * 
		 * @param cmd
		 * @throws Exception
		 */
		public static int exec(String... cmd) throws Exception {
			ProcessBuilder pb = new ProcessBuilder(new String[0]);
			pb.redirectErrorStream(true);
			pb.command(cmd);
			Process ps = pb.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					ps.getInputStream()));
			String s = null;
			while ((s = in.readLine()) != null) {
				System.out.println("------------输出:" + s);
			}
			in.close();
			int code = ps.waitFor();
			if (code == 0) {
				System.out.println("视频转码完成---------");
			} else {
				System.out.println("视频转码失败---------");
				throw new Exception("exec error,code " + code);
			}
			ps.destroy();
			return code;
		}
		
		
		// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		public void run() {
			try {
				String complex = "";
				StringBuffer convert = new StringBuffer();
				convert.append("/usr/bin/ffmpeg");
		        convert.append(" -i "); // 添加参数＂-i＂，该参数指定要转换的文件
		        convert.append(this.putFile); // 添加要转换格式的视频文件的路径
		        if(this.userImg1 != null){
		        	convert.append(" -i ");
			        convert.append(this.userImg1); 
			        complex = "overlay=10:10";
		        }
		        if(this.userImg2 != null){
		        	convert.append(" -i ");
			        convert.append(this.userImg2); 
			        complex = "overlay=main_w-overlay_w-10:main_h-overlay_h-10";
		        }
		        if(this.userImg1 != null && this.userImg2 != null){
		        	complex = "[1:v]scale=150:100[img1];[2:v]scale=150:100[img2];[0:v][img1]overlay=10:10[bkg];[bkg][img2]overlay=main_w-overlay_w-10:main_h-overlay_h-10";
		        } 
		        convert.append(" -acodec "); 
		        convert.append("copy"); 
		        convert.append(" -b:v ");     //指定转换的质量
		        convert.append("600k");
		        convert.append(" -s ");          //设置分辨率
		        convert.append(this.scale);
		        convert.append(" -r ");        //设置帧频
		        convert.append("24");
		        if(this.userImg1 != null || this.userImg2 != null){
		        	 convert.append(" -filter_complex ");
				     convert.append("\""+complex+"\"");
		        }
		        convert.append(" -y "); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
		        convert.append(this.outFile);
		        System.out.println("执行转码===="+convert.toString());
		        int code = VideoUtil.exec("/bin/sh","-c",convert.toString());
		        if(code==0){
					new File(this.putFile).delete();
					new File(this.outFile).renameTo(new File(this.putFile));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
