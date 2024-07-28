package com.skylynx13.transpath.utils;

import com.skylynx13.transpath.log.TransLog;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

/**
 * ClassName: ImageUtils
 * Description: Image utils
 * Date: 2017-10-05 22:30:31
 * @author skylynx
 */
public class ImageUtils {
    
    public static void main(String[] args) {
/*
        System.out.println("123");

        //设置图片宽度相同
        changeImage("D:/imgs/", "1.jpg", "1.jpg", 300,200);
        changeImage("D:/imgs/", "2.jpg", "2.jpg", 300,200);
        changeImage("D:/imgs/", "3.jpg", "3.jpg", 300,200);
        //获取宽度相同的图片
        String img1 = "D:/imgs/1.jpg";
        String img2 = "D:/imgs/2.jpg";
        String img3 = "D:/imgs/3.jpg";
        String[] imgs = new String[]{img1,img2,img3};
        //图片拼接
        merge(imgs,"jpg","D:/imgs/big.jpg");
*/

        String folderPath = "D:/temp/img";
        changeFolderImages(folderPath,5000,5000);
        
        mergeFolderImgs(folderPath,"jpg","D:/temp/img/merge.jpg");
    
    }
    /**
     * 合并图片
     * @param folderPath 图片所在文件夹的绝对路径
     * @param imgType 合并后的图片类型（jpg、png...）
     * @param outAbsolutePath （输出合并后文件的绝对路径）
     */
    private static void mergeFolderImgs(String folderPath, String imgType, String outAbsolutePath){
        File folder = new File(folderPath);
        File[] imgList = folder.listFiles();
        String[] imgPaths = new String[Objects.requireNonNull(imgList).length];
        
        for (int i = 0; i < imgList.length; i++) {
            //System.out.println("文件个数："+imgList[i].length());
            imgPaths[i] = imgList[i].getAbsolutePath();
            System.out.println("第"+i+"张图片途径："+imgPaths[i]);
        }
        merge(imgPaths,imgType,outAbsolutePath);
        
        System.out.println("---------------------");
        File newImg = new File(outAbsolutePath);
        System.out.println(newImg.getName());
    }
    
    
    /**
     * 设置图片大小（单张图片）
     * @param path 路径
     * @param oldimg 旧图片名称
     * @param newimg 新图片名称
     * @param newWidth 新图片宽度
     * @param newHeight 新图片高度
     */
    @SuppressWarnings("unused")
    public static void changeImage(String path, String oldimg, String newimg, int newWidth, int newHeight) {
           try {
               File file = new File(path + oldimg);
               Image img = ImageIO.read(file);
               // 构造Image对象
//               int wideth = img.getWidth(null); // 得到源图宽
//               int height = img.getHeight(null); // 得到源图长
               BufferedImage tag = new BufferedImage(newWidth, newHeight,
                      BufferedImage.TYPE_INT_RGB);
               // 绘制后的图
               tag.getGraphics()
                      .drawImage(img, 0, 0, newWidth, newHeight, null);
               //FileOutputStream out = new FileOutputStream(path + newimg);
               //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
               //encoder.encode(tag); // 近JPEG编码
               //out.close();
               String dstName = path + newimg;
               String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);
               ImageIO.write(tag, /*"GIF"*/ formatName /* format desired */ , new File(dstName) /* target */ );
           } catch (IOException e) {
               System.out.println("处理文件出现异常");
               TransLog.getLogger().error("", e);
           }
        }
    
    
    /**
     * 设置图片大小（批量处理整个文件夹中的图片）
     * @param folderPath 文件夹路径
     * @param newWidth 新图片宽度
     * @param newHeight 新图片高度
     */
    private static void changeFolderImages(String folderPath, int newWidth, int newHeight) {
           try {
               File folder = new File(folderPath);//得到文件夹
               File[] imgList = folder.listFiles();//得到文件夹中的所有图片
               Image image;//定义一张图片
               
               BufferedImage bfImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
//               FileOutputStream outputStream = null;
//               JPEGImageEncoder encoder = null;
               for (int i = 0; i < Objects.requireNonNull(imgList).length; i++) {
                   image = ImageIO.read(imgList[i]);//将得到的图片放入新定义的图片中
                   bfImg.getGraphics().drawImage(image, 0, 0, newWidth, newHeight, null);//绘制后的图
//                   outputStream = new FileOutputStream(imgList[i]);
//                   encoder = JPEGCodec.createJPEGEncoder(outputStream);
//                   encoder.encode(bfImg);
                   String dstName = imgList[i].getName();
                   String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);
                   ImageIO.write(bfImg, /*"GIF"*/ formatName /* format desired */ , new File(dstName) /* target */ );
               }
//               outputStream.close();
           } catch (IOException e) {
               System.out.println("处理文件出现异常");
               TransLog.getLogger().error("", e);
           }
        }
    
    
    /** 
     * Java拼接多张图片 
     *  
     * @param pics :图片源文件 （必须要宽度一样），如：
     *                         String img1 = "D:/imgs/3.jpg";
     *                        String img2 = "D:/imgs/3.jpg";
     *                        String img3 = "D:/imgs/big.jpg";
     *                        String[] pics = new String[]{img1,img2,img3};
     * @param type ：图片输出类型（jpg，png，jpeg...）
     * @param dstPic ：图片输出绝对路径，如 String dst_pic="D:/imgs/big2.jpg";
     */
    private static void merge(String[] pics, String type, String dstPic) {
  
        int len = pics.length;  //图片文件个数
        if (len < 1) {  
            System.out.println("pics len < 1");  
            return;
        }  
        File[] src = new File[len];  
        BufferedImage[] images = new BufferedImage[len];  
        int[][] imageArrays = new int[len][];
        for (int i = 0; i < len; i++) {  
            try {  
                src[i] = new File(pics[i]);  
                images[i] = ImageIO.read(src[i]);  
            } catch (Exception e) {
                TransLog.getLogger().error("", e);
                return;
            }  
            int width = images[i].getWidth();  
            int height = images[i].getHeight();  
            imageArrays[i] = new int[width * height];// 从图片中读取RGB
            imageArrays[i] = images[i].getRGB(0, 0, width, height,
                    imageArrays[i], 0, width);
        }  
  
        int dstHeight = 0;
        int dstWidth = images[0].getWidth();
        for (BufferedImage image : images) {
            dstWidth = Math.max(dstWidth, image.getWidth());

            dstHeight += image.getHeight();
        }  
        System.out.println(dstWidth);
        System.out.println(dstHeight);
        if (dstHeight < 1) {
            System.out.println("dst_height < 1");  
            return;
        }  
  
        // 生成新图片   
        try {  
            // dst_width = images[0].getWidth();   
            BufferedImage imageNew = new BufferedImage(dstWidth, dstHeight,
                    BufferedImage.TYPE_INT_RGB);  
            int heightI = 0;
            for (int i = 0; i < images.length; i++) {  
                imageNew.setRGB(0, heightI, dstWidth, images[i].getHeight(),
                        imageArrays[i], 0, dstWidth);
                heightI += images[i].getHeight();
            }  
  
            File outFile = new File(dstPic);
            ImageIO.write(imageNew, type, outFile);// 写图片
        } catch (Exception e) {
            TransLog.getLogger().error("", e);
        }
    }
}
