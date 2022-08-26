package com.kubian.mode.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author WangW
 * @date 2017-12-21下午16:15:08
 */

public class MyTools {
	/**
	 * 
	 * 获取指定视频的帧并保存为图片至指定目录
	 * 
	 * @param videofile
	 *            源视频文件路径
	 * 
	 * @param framefile
	 *            截取帧的图片存放路径
	 * 
	 * @throws Exception
	 * 
	 */

	public static void fetchFrame(String videofile, String framefile)

			throws Exception {

		long start = System.currentTimeMillis();

		File targetFile = new File(framefile);
		
		FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);

		ff.start();

		int lenght = ff.getLengthInFrames();

		int i = 0;

		Frame f = null;

		while (i < lenght) {

			// 过滤前5帧，避免出现全黑的图片，依自己情况而定

			f = ff.grabFrame();

			if ((i > 2) && (f.image != null)) {

				break;

			}

			i++;

		}

		IplImage img = f.image;

		int owidth = img.width();

		int oheight = img.height();

		// 对截取的帧进行等比例缩放

		int width = 800;

		int height = (int) (((double) width / owidth) * oheight);

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),

				0, 0, null);

		ImageIO.write(bi, "jpg", targetFile);

		// ff.flush();

		ff.stop();

		System.out.println(System.currentTimeMillis() - start);

	}

	// 获取一个六位数的随机验证码
	public static String getCode() {
		String sms = "";
		int[] nums = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int count = 0;
		for (int i = 0; i < nums.length; i++) {
			sms += new Random().nextInt(nums.length);
			count++;
			if (count == 6) {
				break;
			}
		}
		return sms;
	}

	// 判断对象里的某个属性是否存在空值
	public static Set<String> isAllFieldNull(Object obj) throws Exception {
		Set<String> set = new HashSet<String>();
		Class stuCla = (Class) obj.getClass();// 得到类对象
		Field[] fs = stuCla.getDeclaredFields();// 得到属性集合
		for (Field f : fs) {// 遍历属性
			f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
			Object val = f.get(obj);// 得到此属性的值
			if (val == null) {// 只要有1个属性不为空,那么就不是所有的属性值都为空
				set.add(f.getName());
			}
		}
		return set;
	}

	// 判断对象是否为空
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if ((obj instanceof List)) {
			return ((List) obj).size() == 0;
		}
		if ((obj instanceof String)) {
			return ((String) obj).trim().equals("");
		}
		return false;
	}

	// 获取请求者IP地址
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static boolean updateNotNullField(Object rawObject, Object newObject)
			throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		// 如果连个对象不一致。不进行更新字段值的操作
		if (rawObject.getClass().getName() != newObject.getClass().getName()) {
			return false;
		}
		// 获取原始对象中的所有public方法
		Method[] methods = rawObject.getClass().getDeclaredMethods();
		// 用于提取不包含指定关键词的方法
		String regExpression = "^(get)(?!Id|patientCount|appointmentTime)(\\w+)";
		Pattern pattern = Pattern.compile(regExpression);
		Matcher m;
		for (Method method : methods) {
			m = pattern.matcher(method.getName());
			// 正则匹配以get开头，后面不能匹配Id、CreateTime这两个单词的方法
			if (m.find()) {
				Object o = method.invoke(newObject, null);
				// 忽略值为空的字段
				if (o == null) {
					continue;
				}
				// 取出get方法名后面的字段名
				String fieldName = m.group(2);
				// 找到该字段名的set方法
				Method rawMethod = rawObject.getClass().getMethod("set" + fieldName, method.getReturnType());
				// 调用实体对象的set方法更新字段值
				rawMethod.invoke(rawObject, o);
			}
		}
		return true;
	}

	public static boolean updateNotNullFieldForPatient(Object rawObject, Object newObject)
			throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		// 如果连个对象不一致。不进行更新字段值的操作
		if (rawObject.getClass().getName() != newObject.getClass().getName()) {
			return false;
		}
		// 获取原始对象中的所有public方法
		Method[] methods = rawObject.getClass().getDeclaredMethods();
		// 用于提取不包含指定关键词的方法
		String regExpression = "^(get)(?!Id)(\\w+)";
		Pattern pattern = Pattern.compile(regExpression);
		Matcher m;
		for (Method method : methods) {
			m = pattern.matcher(method.getName());
			// 正则匹配以get开头，后面不能匹配Id、CreateTime这两个单词的方法
			if (m.find()) {
				Object o = method.invoke(newObject, null);
				// 忽略值为空的字段
				if (o == null) {
					continue;
				}
				if ("" .equals(o)) {
					continue;
				}
				// 取出get方法名后面的字段名
				String fieldName = m.group(2);
				// 找到该字段名的set方法
				Method rawMethod = rawObject.getClass().getMethod("set" + fieldName, method.getReturnType());
				// 调用实体对象的set方法更新字段值
				rawMethod.invoke(rawObject, o);
			}
		}
		return true;
	}

	// 判断是否为图片
	public static Boolean isImageFile(String filesName) {
		String[] img_type = new String[] { ".jpg", ".jpeg", ".png", ".gif", ".bmp" };
		if (filesName == null) {
			return false;
		}
		filesName = filesName.toLowerCase();
		for (String type : img_type) {
			if (filesName.endsWith(type)) {
				return true;
			}
		}
		return false;
	}

	// 获取文件后缀名
	public static String getFileType(String fileName) {
		if (fileName != null && fileName.indexOf(".") >= 0) {
			return fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
		}
		return "";
	}

	// 获取文件名
	public static String getFileName(String fileName) {
		if (fileName != null && fileName.indexOf(".") >= 0) {
			return fileName.substring(0, fileName.lastIndexOf(".")).toLowerCase();
		}
		return "";
	}

	/*
	 * 将时间转换为时间戳
	 */
	public static String dateToStamp(String s) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static Date stampToDate(String s) {
		long lt = new Long(s);
		Date date = new Date(lt);
		return date;
	}

	/**
	 * 判断是否为真实手机号
	 *
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobile(String mobiles) {
		Pattern p = Pattern.compile("/^1[345678]\\d{9}$/");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static List<java.util.Date> dateToWeek(java.util.Date mdate) {
		int b = mdate.getDay();
		java.util.Date fdate;
		List<java.util.Date> list = new ArrayList();
		Long fTime = mdate.getTime() - b * 24 * 3600000;
		for (int a = 0; a < 7; a++) {
			fdate = new java.util.Date();
			fdate.setTime(fTime + (a * 24 * 3600000));
			list.add(a, fdate);
		}

		return list;
	}

	public static List<java.util.Date> getDateAfter(java.util.Date d) {
		List<java.util.Date> list = new ArrayList<java.util.Date>();
		for (int i = 1; i <= 7; i++) {
			Calendar now = Calendar.getInstance();
			now.setTime(d);
			now.set(Calendar.DATE, now.get(Calendar.DATE) + i);
			list.add(now.getTime());
		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println(MyTools.getSerialNo());
	}

	// 保存图片和视频
	public static String saveFiles(MultipartFile multipartFile, String uploadPath, String webImgPath) {
		String FileType = MyTools.getFileType(multipartFile.getOriginalFilename());
		String imgName = UUID.randomUUID().toString() + FileType;
		File files = new File(uploadPath + imgName);
		try {
			FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), files);
			System.out.println(files.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			return "图片保存出错!";
		}
		return webImgPath + imgName;
	}

	public static String getSerialNo() {
		String sixChar = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		java.util.Date date = new java.util.Date();
		String time = sdf.format(date);
		for (int i = 0; i < time.length() / 2; i++) {
			String singleChar;
			String x = time.substring(i * 2, (i + 1) * 2);
			int b = Integer.parseInt(x);
			if (b < 10) {
				singleChar = Integer.toHexString(Integer.parseInt(x));
			} else if (b >= 10 && b < 36) {
				singleChar = String.valueOf((char) (Integer.parseInt(x) + 55));
			} else {
				singleChar = String.valueOf((char) (Integer.parseInt(x) + 61));
			}
			sixChar = sixChar + singleChar;

		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return StringUtils.split(df.format(new java.util.Date()), " ")[0].replace("-", "") + StringUtils.split(df.format(new java.util.Date()), " ")[1].replace(":", "") + sixChar;
	}

}
