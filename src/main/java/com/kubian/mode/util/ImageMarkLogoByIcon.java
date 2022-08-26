package com.kubian.mode.util;

import java.awt.AlphaComposite;   
import java.awt.Graphics2D;   
import java.awt.Image;   
import java.awt.RenderingHints;   
import java.awt.image.BufferedImage;   
import java.io.File;   
import java.io.FileInputStream;
import java.io.FileOutputStream;   
import java.io.InputStream;
import java.io.OutputStream;   
import java.util.Iterator;
  
import java.awt.Rectangle;  
import java.io.IOException;  
  
import javax.imageio.ImageIO;  
import javax.imageio.ImageReadParam;  
import javax.imageio.ImageReader;  
import javax.imageio.stream.ImageInputStream;  
import javax.swing.ImageIcon;

import com.kubian.mode.AppUser;

  
  
/**  
 * 图片水印  
 * @blog http://sjsky.iteye.com  
 * @author Michael  
 */  
public class ImageMarkLogoByIcon {   
  
    /**  
     * @param args  
     * @throws IOException 
     */  
    public static void main(String[] args) throws IOException {   
        String srcImgPath = "C:/Users/Administrator/Desktop/mu/1527814808643.jpg";   
        String iconPath = "C:/Users/Administrator/Desktop/mu/yh.jpg";   
        String targerPath = "C:/Users/Administrator/Desktop/mu/123.jpg";   
        String targerPath2 = "C:/Users/Administrator/Desktop/图片/cat640_1.jpg";   
        AppUser au = new AppUser();
        au.setId(1l);
        // 给图片添加水印   
        ImageMarkLogoByIcon.markImageByIcon(au,iconPath, srcImgPath, targerPath);   
        // 给图片添加水印,水印旋转-45   
        //ImageMarkLogoByIcon.markImageByIcon(iconPath, srcImgPath, targerPath2,-45);   
//        ImageMarkLogoByIcon.cutCenterImage(targerPath,targerPath2);
    }   
  
    /**  
     * 给图片添加水印  
     * @param iconPath 水印图片路径  
     * @param srcImgPath 源图片路径  
     * @param targerPath 目标图片路径  
     */  
    public static void markImageByIcon(AppUser au,String iconPath, String srcImgPath,   
            String targerPath) {   
        markImageByIcon(au,iconPath, srcImgPath, targerPath, null);   
    }   
  
    /* 
     * 根据尺寸图片居中裁剪 
     */  
     public static void cutCenterImage(String src,String dest) throws IOException{
         Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
         ImageReader reader = (ImageReader)iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
         int imageIndex = 0;   
         int w = reader.getWidth(0);
         int h = reader.getHeight(0)/3;
         Rectangle rect = new Rectangle((reader.getWidth(imageIndex)-w)/2, (reader.getHeight(imageIndex)-h)/2, w, h);    
         param.setSourceRegion(rect);   
         BufferedImage bi = reader.read(0,param);     
         ImageIO.write(bi, "jpg", new File(dest));             
     }  
    
    
    /**  
     * 给图片添加水印、可设置水印图片旋转角度  
     * @param iconPath 水印图片路径  
     * @param srcImgPath 源图片路径  
     * @param targerPath 目标图片路径  
     * @param degree 水印图片旋转角度  
     */  
    public static void markImageByIcon(AppUser au,String realPath, String srcImgPath,   
            String targerPath, Integer degree) {   
        OutputStream os = null;   
        try {   
            Image srcImg = ImageIO.read(new File(srcImgPath));   
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),   
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);   
            //ImgUtil.resizeImg(new File(iconPath), "C:\\Temp\\4.jpg",buffImg.getWidth()/10,buffImg.getHeight()/10);
            
            // 得到画笔对象   
            // Graphics g= buffImg.getGraphics();   
            Graphics2D g = buffImg.createGraphics();   
  
            // 设置对线段的锯齿状边缘处理   
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,   
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);   
  
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg   
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);   
  
            if (null != degree) {   
                // 设置水印旋转   
                g.rotate(Math.toRadians(degree),   
                        (double) buffImg.getWidth() / 2, (double) buffImg   
                                .getHeight() / 2);   
            }   
            int bfW = buffImg.getWidth();
            int bfH = buffImg.getHeight();
            String iconPath="";
            String userIcon=null;
            String parent = new File(realPath).getParent();
            if(bfW>=900){
            	iconPath = realPath+"images/logo1.png";
            	if(au != null && au.getSyImg1() != null){
            		String str = au.getSyImg1();
            		String newStr=str.substring(str.length()-26);
            		userIcon=parent+newStr;
            	}
            }else if(bfW>=600 && bfW<900){
            	iconPath = realPath+"images/logo2.png";
            	if(au != null && au.getSyImg2() != null){
            		String str = au.getSyImg2();
            		String newStr=str.substring(str.length()-26);
            		userIcon=parent+newStr;
            	}
            }else{
            	iconPath = realPath+"images/logo3.png";
            	if(au != null && au.getSyImg3() != null){
            		String str = au.getSyImg3();
            		String newStr=str.substring(str.length()-26);
            		userIcon=parent+newStr;
            	}
            }
            System.out.println("userIcon----------"+userIcon);
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度   
            ImageIcon imgIcon = new ImageIcon(iconPath);
            // 得到Image对象。   
            Image img = imgIcon.getImage();
            float alpha = 0.5f; // 透明度   
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,   
                    alpha));   
            // 表示水印图片的位置 ，宽、高随主图大小变化
            g.drawImage(img, 7, 7,null);
            if(userIcon != null){
            	ImageIcon userImgIcon = new ImageIcon(userIcon);
            	Image auimg = userImgIcon.getImage();
            	int uw = auimg.getWidth(null);
            	int uh = auimg.getHeight(null);
            	g.drawImage(auimg, bfW-uw-7, bfH-uh-7,null);
            }
            
  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));   
  
            g.dispose();   
  
            os = new FileOutputStream(targerPath);   
  
            // 生成图片   
            ImageIO.write(buffImg, "JPG", os);   
  
            System.out.println("图片完成添加Icon印章。。。。。。");   
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
            try {   
                if (null != os)   
                    os.close();   
            } catch (Exception e) {   
                e.printStackTrace();   
            }   
        }   
    }   
    
}  

