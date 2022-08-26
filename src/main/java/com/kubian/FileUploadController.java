package com.kubian;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacpp.avcodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.kubian.mode.AppUser;
import com.kubian.mode.MultipartFileParam;
import com.kubian.mode.User;
import com.kubian.mode.dao.AppUserDao;
import com.kubian.mode.dao.UserDao;
import com.kubian.mode.util.AudioConvert;
import com.kubian.mode.util.AudioConvertUtils;
import com.kubian.mode.util.ImageRemarkUtil;
import com.kubian.mode.util.MyTools;
import com.kubian.mode.util.VideoUtil;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

@RestController
public class FileUploadController {

	private final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	private static AtomicLong counter = new AtomicLong(0L);

	@Value("${web.upload.path}")
	private String uploadPath;
	@Value("${web.img.path}")
	private String webImgPath;
	@Value("${serverIP}")
	private String serviceIP;
	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private UserDao userDao;

	@RequestMapping("uploadfile")
	public Object uploadv2(MultipartFileParam param) throws Exception {

		System.out.println("------------------------------------------------");
		String path = null;
		String tempDirPath = "";
		String mp3Path = "";
		try {

			String prefix = "req_count:" + counter.incrementAndGet() + ":";
			System.out.println(prefix + "start !!!");
			// 使用 工具类解析相关参数，工具类代码见下面

			System.out.println();
			System.out.println();
			System.out.println();

			logger.info(prefix + "chunks= " + param.getChunks());
			logger.info(prefix + "chunk= " + param.getChunk());
			logger.info(prefix + "chunkSize= " + param.getSize());

			// 这个必须与前端设定的值一致
			long chunkSize = 1024 * 1024 * 500;

			if (!MyTools.isEmpty(param.getId())) {
				tempDirPath = uploadPath + "/img/" + param.getId();
			} else {
				tempDirPath = uploadPath + "/video";
			}

			String tempFileName = param.getName();

			File confFile = new File(tempDirPath, param.getName() + ".conf");

			File tmpDir = new File(tempDirPath);

			File tmpFile = new File(tempDirPath, tempFileName);

			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");

			RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");

			long offset = chunkSize * param.getChunk();
			// 定位到该分片的偏移量
			accessTmpFile.seek(offset);
			// 写入该分片数据
			accessTmpFile.write(param.getFile().getBytes());

			// 把该分段标记为 true 表示完成
			System.out.println(prefix + "set part " + param.getChunk() + " complete");

			accessConfFile.setLength(param.getChunks());
			accessConfFile.seek(param.getChunk());
			accessConfFile.write(Byte.MAX_VALUE);

			// completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
			byte[] completeList = FileUtils.readFileToByteArray(confFile);
			byte isComplete = Byte.MAX_VALUE;
			for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
				// 与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
				isComplete = (byte) (isComplete & completeList[i]);

				System.out.println(prefix + "check part " + i + " complete?:" + completeList[i]);
			}

			if (isComplete == Byte.MAX_VALUE) {

				System.out.println(
						prefix + "upload complete !!" + "---------------------------------------------------------");
				renameFile(tempDirPath + "/" + tempFileName, tempDirPath + "/" + param.getName());
				if (MyTools.isEmpty(param.getId())) {
					path = "video/" + param.getName();
					mp3Path = "video/";
				} else {
					path = "img/" + param.getId() + "/" + param.getName();
					mp3Path = "img/" + param.getId() + "/";
				}

			}

			accessTmpFile.close();
			accessConfFile.close();

			System.out.println(prefix + "end !!!");

		} catch (Exception e) {
			e.printStackTrace();
			return "上传失败!";
		}

		if (path != null) {

			String returnPath = "";
			try {
				returnPath = webImgPath + path;
				System.out.println("文件所在地址：" + uploadPath + path);
				// 文件后缀名
				String suffix = MyTools.getFileType(param.getName());
				List<String> mp3s = new ArrayList<String>();
				mp3s.add(".ogg");
				mp3s.add(".asf");
				mp3s.add(".wma");
				mp3s.add(".wav");
				mp3s.add(".rm");
				mp3s.add(".real");
				mp3s.add(".ape");
				mp3s.add(".mp3pro");
				mp3s.add(".module");
				mp3s.add(".midi");
				mp3s.add(".vqf");
				mp3s.add(".m4a");
				mp3s.add(".aac");
				mp3s.add(".mpc");
				mp3s.add(".amr");
				mp3s.add(".3gpp");
				mp3s.add(".quicktime");
				for (String string : mp3s) {
					if (string.equals(suffix)) {
						System.out.println("上传文件所在地址：" + returnPath);
						String result = AudioConvertUtils.changeRemoteSourceToMp3(returnPath, uploadPath);
						String fileName = MyTools.getFileName(param.getName()); // 文件名(没有后缀名)
						fileName = fileName + ".mp3";
						System.out.println("转化的文件所在地址：" + webImgPath + fileName);
						return webImgPath + fileName;
						// String filePath = tempDirPath + File.separator +
						// param.getName(); // 文件上传成功后所在的绝对路径
						//
						// String fileName =
						// MyTools.getFileName(param.getName()); // 文件名(没有后缀名)
						// fileName = fileName + ".mp3";
						// String newFilePath = tempDirPath + File.separator +
						// fileName; // 转换成MP3格式后所在的绝对路径
						// AudioConvert.convert(filePath, newFilePath,
						// avcodec.AV_CODEC_ID_MP3, 8000, 16, 1);
						// System.out.println(webImgPath + mp3Path + fileName);
						// // throw new Exception("输入错误");
						// return webImgPath + mp3Path + fileName;
					}
				}
				List<String> suffixs = new ArrayList<String>();
				suffixs.add(".mp4");
				suffixs.add(".mpg");
				suffixs.add(".mpeg");
				suffixs.add(".avi");
				suffixs.add(".rm");
				suffixs.add(".rmvb");
				suffixs.add(".asf");
				suffixs.add(".dat");
				for (String string : suffixs) {
					if (string.equals(suffix)) {
						String fileName = MyTools.getFileName(param.getName()) + ".jpg";
						// 新建文件
						File myFile = new File(tempDirPath, fileName);
						myFile.createNewFile();
						// 视频文件路径
						String filePath = tempDirPath + "/" + param.getName();
						// 第一帧图片存放地址
						String imgPath = "";
						if (!MyTools.isEmpty(param.getId())) {
							imgPath = uploadPath + "img/" + param.getId() + "/" + fileName;
						} else {
							imgPath = uploadPath + "video/" + fileName;
						}

						// 截取第一帧图片
						MyTools.fetchFrame(filePath, imgPath);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "上传失败!";
			}
			System.out.println("返回值：" + returnPath);
			return returnPath;
		}

		return "还在上传中";
	}

	private void renameFile(String file, String toFile) {
		File toBeRenamed = new File(file);
		// 检查要重命名的文件是否存在，是否是文件
		if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {

			System.out.println("File does not exist: " + file);
			return;
		}

		File newFile = new File(toFile);

		// 修改文件名
		if (toBeRenamed.renameTo(newFile)) {
			System.out.println("File has been renamed.");
		} else {
			System.out.println("Error renmaing file");
		}

	}

	public static void inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("uploadfile1")
	public Object uploadv1(MultipartFileParam param, HttpSession session) throws Exception {
		System.out.println("------------------------------------------------");
		String path = null;
		String tempDirPath = "";
		String mp3Path = "";
		try {

			String prefix = "req_count:" + counter.incrementAndGet() + ":";
			System.out.println(prefix + "start !!!");
			// 使用 工具类解析相关参数，工具类代码见下面

			System.out.println();
			System.out.println();
			System.out.println();

			logger.info(prefix + "chunks= " + param.getChunks());
			logger.info(prefix + "chunk= " + param.getChunk());
			logger.info(prefix + "chunkSize= " + param.getSize());

			// 这个必须与前端设定的值一致
			long chunkSize = 1024 * 1024 * 500;

			if (!MyTools.isEmpty(param.getId())) {
				tempDirPath = uploadPath + "/img/";
			} else {
				tempDirPath = uploadPath + "/video";
			}

			String tempFileName = param.getName();

			File confFile = new File(tempDirPath, param.getName() + ".conf");

			File tmpDir = new File(tempDirPath);

			File tmpFile = new File(tempDirPath, tempFileName);

			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");

			RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");

			long offset = chunkSize * param.getChunk();
			// 定位到该分片的偏移量
			accessTmpFile.seek(offset);
			// 写入该分片数据
			accessTmpFile.write(param.getFile().getBytes());

			// 把该分段标记为 true 表示完成
			System.out.println(prefix + "set part " + param.getChunk() + " complete");

			accessConfFile.setLength(param.getChunks());
			accessConfFile.seek(param.getChunk());
			accessConfFile.write(Byte.MAX_VALUE);

			// completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
			byte[] completeList = FileUtils.readFileToByteArray(confFile);
			byte isComplete = Byte.MAX_VALUE;
			for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
				// 与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
				isComplete = (byte) (isComplete & completeList[i]);

				System.out.println(prefix + "check part " + i + " complete?:" + completeList[i]);
			}

			if (isComplete == Byte.MAX_VALUE) {

				System.out.println(
						prefix + "upload complete !!" + "---------------------------------------------------------");
				renameFile(tempDirPath + "/" + tempFileName, tempDirPath + "/" + param.getName());
				if (MyTools.isEmpty(param.getId())) {
					path = "video/" + param.getName();
					mp3Path = "video/";
				} else {
					path = "img/" + "/" + param.getName();
					mp3Path = "img/" + "/";
				}

			}

			accessTmpFile.close();
			accessConfFile.close();

			System.out.println(prefix + "end !!!");

		} catch (Exception e) {
			e.printStackTrace();
			return "上传失败!";
		}

		if (path != null) {

			String returnPath = "";
			try {
				returnPath = webImgPath + path;
				System.out.println("文件所在地址：" + uploadPath + path);
				// 文件后缀名
				String suffix = MyTools.getFileType(param.getName());
				List<String> mp3s = new ArrayList<String>();
				mp3s.add(".ogg");
				mp3s.add(".asf");
				mp3s.add(".wma");
				mp3s.add(".wav");
				mp3s.add(".rm");
				mp3s.add(".real");
				mp3s.add(".ape");
				mp3s.add(".mp3pro");
				mp3s.add(".module");
				mp3s.add(".midi");
				mp3s.add(".vqf");
				mp3s.add(".m4a");
				mp3s.add(".aac");
				mp3s.add(".mpc");
				mp3s.add(".amr");
				mp3s.add(".3gpp");
				mp3s.add(".quicktime");
				for (String string : mp3s) {
					if (string.equals(suffix)) {
						System.out.println("上传文件所在地址：" + returnPath);
						String result = AudioConvertUtils.changeRemoteSourceToMp3(returnPath, uploadPath);
						String fileName = MyTools.getFileName(param.getName()); // 文件名(没有后缀名)
						fileName = fileName + ".mp3";
						System.out.println("转化的文件所在地址：" + webImgPath + fileName);
						return webImgPath + fileName;
						// String filePath = tempDirPath + File.separator +
						// param.getName(); // 文件上传成功后所在的绝对路径
						//
						// String fileName =
						// MyTools.getFileName(param.getName()); // 文件名(没有后缀名)
						// fileName = fileName + ".mp3";
						// String newFilePath = tempDirPath + File.separator +
						// fileName; // 转换成MP3格式后所在的绝对路径
						// AudioConvert.convert(filePath, newFilePath,
						// avcodec.AV_CODEC_ID_MP3, 8000, 16, 1);
						// System.out.println(webImgPath + mp3Path + fileName);
						// // throw new Exception("输入错误");
						// return webImgPath + mp3Path + fileName;
					}
				}
				List<String> suffixs = new ArrayList<String>();
				suffixs.add(".mp4");
				suffixs.add(".mpg");
				suffixs.add(".mpeg");
				suffixs.add(".avi");
				suffixs.add(".rm");
				suffixs.add(".rmvb");
				suffixs.add(".asf");
				suffixs.add(".dat");
				for (String string : suffixs) {
					if (string.equals(suffix)) {

						if (!VideoUtil.isTimeOut(uploadPath + path, 300)) {
							return "上传失败,视频不能大于五分钟！";
						}
						String simg1 = null; // 左上角水印
						String simg2 = null; // 右下角水印
						User user = null;
						if (!MyTools.isEmpty(param.getId()) && !"null".equals(param.getId())) {
							// 是app用户操作
							String paramId = param.getId();
							paramId = paramId.substring(0, paramId.indexOf("-"));
							Long id = Long.valueOf(paramId); // user用户id
							user = userDao.findById(id);
							if (!MyTools.isEmpty(user)) {
								AppUser appUser = appUserDao.findById(user.getAuId());
								if (!MyTools.isEmpty(appUser)) {
									simg2 = appUser.getSyImg2();
									if (!MyTools.isEmpty(simg2)) {
										simg2 = simg2.substring(simg2.indexOf("KuBianImg/") + 10);
										simg2 = uploadPath + simg2;
									}
								} else {
									simg1 = user.getWatermark();
									if (!MyTools.isEmpty(simg1)) {
										simg1 = simg1.substring(simg1.indexOf("KuBianImg/") + 10);
										simg1 = uploadPath + simg1;
									} else {
										simg1 = uploadPath + "img/kbicon4.png";
									}
									
								}
							}
						}

						System.out.println("左上角水印：" + simg1);
						System.out.println("右下角水印：" + simg2);
						File outFile = new File(uploadPath + "/newPath/");
						if (!outFile.exists()) {
							outFile.mkdirs();
						}
						if (user.getAuId() != null) {
							// 是vip用户
							AppUser appUser = appUserDao.findById(user.getAuId());
							if (!MyTools.isEmpty(appUser)) {
								if (appUser.getIsWatermark() == 0) {
									// 代表用户不使用水印webImgPath
									simg1 = uploadPath + "qwe.png";
									simg2 = uploadPath + "qwe.png";
								}
							}
						}
						VideoUtil vf = new VideoUtil(uploadPath + path, uploadPath + param.getName(), simg1, simg2);
						new Thread(vf).start();
						System.out.println("原始视频地址：" + uploadPath + path);
						System.out.println("加了水印的视频地址：" + uploadPath + param.getName());

						String fileName = MyTools.getFileName(param.getName()) + ".jpg";
						// 新建文件
						File myFile = new File(tempDirPath, fileName);
						myFile.createNewFile();
						// 视频文件路径
						String filePath = uploadPath + path;
						// 第一帧图片存放地址
						String imgPath = "";
						if (!MyTools.isEmpty(param.getId())) {
							imgPath = uploadPath + "img/" + "/" + fileName;
						} else {
							imgPath = uploadPath + "video/" + fileName;
						}

						// 截取第一帧图片
						MyTools.fetchFrame(filePath, imgPath);
					}
				}
				
				
				// 给上传的图片加水印
				List<String> imgsuffixs = new ArrayList<String>();
				imgsuffixs.add(".bmp");
				imgsuffixs.add(".jpeg");
				imgsuffixs.add(".png");
				imgsuffixs.add(".gif");
				imgsuffixs.add(".tiff");
				imgsuffixs.add(".jpg");
				imgsuffixs.add(".psd");
				
				for (String string : imgsuffixs) {
					if (string.equals(suffix)) {
						User user = null;
						if (!MyTools.isEmpty(param.getId()) && !"null".equals(param.getId())) {
							String simg1 = null; // 左上角水印
							String simg2 = null; // 右下角水印
							// 是app用户操作
							String paramId = param.getId();
							paramId = paramId.substring(0, paramId.indexOf("-"));
							Long id = 0l;
							if (!MyTools.isEmpty(paramId) && !"null".equals(paramId)) {
								id = Long.valueOf(paramId); // user用户id
							}
							user = userDao.findById(id);
							if (!MyTools.isEmpty(user)) {
								AppUser appUser = appUserDao.findById(user.getAuId());
								if (!MyTools.isEmpty(appUser)) {
									simg2 = appUser.getSyImg2();
									if (!MyTools.isEmpty(simg2)) {
										simg2 = simg2.substring(simg2.indexOf("KuBianImg/") + 10);
										simg2 = uploadPath + simg2;
									}
								} else {
									simg1 = user.getWatermark();
									if (!MyTools.isEmpty(simg1)) {
										simg1 = simg1.substring(simg1.indexOf("KuBianImg/") + 10);
										simg1 = uploadPath + simg1;
									} else {
										simg1 = uploadPath + "img/kbicon4.png";
									}
									
								}

							String imgFile = (uploadPath + path).substring((uploadPath + path).indexOf("KuBianImg/") + 10);
							if (user.getAuId() != null) {
								// 是vip用户
								AppUser appUser2 = appUserDao.findById(user.getAuId());
								if (!MyTools.isEmpty(appUser2)) {
									if (appUser2.getIsWatermark() != null && appUser2.getIsWatermark() == 0) {
										// 代表用户不使用水印webImgPath
										simg1 = uploadPath + "qwe.png";
										simg2 = uploadPath + "qwe.png";
									}
								}
							}
						}
							System.out.println("左上角水印：" + simg1);
							System.out.println("右下角水印：" + simg2);
							System.out.println("源图片：" +  uploadPath + path);
							if (!MyTools.isEmpty(simg1)) {
								// 只添加左上角水印
								ImageRemarkUtil.markImageByIcon(simg1, uploadPath + path, uploadPath + path);
							}
							if (!MyTools.isEmpty(simg2)) {
								// 只添加右上角水印
								ImageRemarkUtil.markImageByIcon1(simg2, uploadPath + path, uploadPath + path);
							}
							
						}
						break;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return "上传失败!";
			}
			System.out.println("返回值：" + returnPath);
			return returnPath;
		}

		return "还在上传中";
	}
}
