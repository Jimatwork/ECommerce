package test;

import java.io.File;
import java.io.IOException;

public class TestSub {
	public static String upFileImg(String dirName, File file) throws IOException {
		String newFile = "C:/gz/MyEcpliseWork/KuBian/target/classes/static/roomimg/img/WU_FILE_0";
		String fname = System.currentTimeMillis() + ".jpg";
		// 按比例缩小
		ImgUtil.resizeImg(file, newFile + "/" + fname, 200, 200);
		// FileUtils.copyFile(myUploadImg, new File(newFile,fname));
		String serverURL = "http://192.168.10.105:8888/";
		String prImg = serverURL + "/" + fname;
		return prImg;
	}

	public static void main(String[] args) {

//		String s = "http://kuedit.21www.net/iconImg/1515740687950.jpg";
//		int i = s.indexOf("KuBianImg/");
//		System.out.println(i);

		// String s =
		// "http://192.168.10.105:8888/roomimg/img/WU_FILE_0/123.jpg";
		// s = s.substring(s.indexOf("roomimg/")+8);
//		System.out.println(s);
		// File file = new
		// File("C:/gz/MyEcpliseWork/KuBian/target/classes/static/roomimg/img/WU_FILE_0/1d57c174310958788571ec30a8828cb2.jpg");
		// try {
		// System.out.println(TestSub.upFileImg("123", file));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		
//		int num = new Random().nextInt(10) + 1;
//		System.out.println(num);

		for (int i = 1 ; i<=200 ; i++) {
			String str = "km";
			if (i < 10) {
				str = str + "0" + i;

			} else {
				str = str  + i;
			}
			System.out.println(str);
		}

	}
}
