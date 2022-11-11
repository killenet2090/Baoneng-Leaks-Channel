package com.bnmotor.icv.tsp.commons.oss.util;


import com.bnmotor.icv.tsp.commons.oss.constant.OssConstant;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author :luoyang
 * @date_time :2020/12/28 14:17
 * @desc:
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class PicUtil {

    private PicUtil() {
        throw new RuntimeException("创建异常");
    }

    public static int rgbY = 3;
    public static int rgbY1 = 3;
    public static int rgbX = 3;
    public static int rgbX1 = 3;
    public static int boundary = 10;
    public static int frame_size = 5;
    public static int start_frame = 30;

    /**
     * 获取图片黑边上边界
     *
     * @param bufferedImage
     * @return
     */
    public static Integer getPicY1(BufferedImage bufferedImage) {
        try {
            int y1 = 0;
            int widthCount = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (R <= rgbY && G <= rgbY && B <= rgbY) {
                        y1 = j1;
                        widthCount++;
                        if (widthCount == bufferedImage.getWidth()) {
                            widthCount = 0;
                           // log.info(" 上边界 y1： R = {}, G = {}, B = {} ",R , G , B);
                        }
                    } else {
                        return y1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取图片黑边下边界
     *
     * @param bufferedImage
     * @param start
     * @return
     */
    public static Integer getPicY2(BufferedImage bufferedImage, int start) {
        int height = 0;
        try {
            int y1;
            int widthCount = 0;
            height = bufferedImage.getHeight();
            for (int j1 = start + boundary; j1 < height; j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (R <= rgbY1 && G <= rgbY1 && B <= rgbY1) {
                        y1 = j1;
                        widthCount++;
                        if (widthCount == bufferedImage.getWidth()) {
                            //log.info(" 下边界 y2： R = {}, G = {}, B = {} ",R , G , B);
                            return y1;
                        }
                    } else {
                        widthCount = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 获取图片黑边左边界
     *
     * @param bufferedImage
     * @return
     */
    public static Integer getPicX1(BufferedImage bufferedImage) {
        try {
            int x1 = 0;
            int widthCount = 0;
            for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (R <= rgbX && G <= rgbX && B <= rgbX) {
                        x1 = j2;
                        widthCount++;
                        if (widthCount == bufferedImage.getHeight()) {
                            widthCount = 0;
                           // log.info(" 左边界 x1： R = {}, G = {}, B = {} ",R , G , B);
                        }
                    } else {
                        return x1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取图片黑边下边界
     *
     * @param bufferedImage
     * @param start
     * @return
     */
    public static Integer getPicX2(BufferedImage bufferedImage, int start) {
        int width = 0;
        try {
            int x1;
            int widthCount = 0;
            width = bufferedImage.getWidth();
            for (int j2 = start + boundary; j2 < width; j2++) {
                for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (R <= rgbX1 && G <= rgbX1 && B <= rgbX1) {
                        x1 = j2;
                        widthCount++;
                        if (widthCount == bufferedImage.getHeight()) {
                           // log.info(" 右边界 x2： R = {}, G = {}, B = {} ",R , G , B);
                            return x1;
                        }
                    } else {
                        widthCount = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return width;
    }

    /**
     * 图片转字节
     *
     * @param bufferedImage
     * @return
     */
    public static ByteArrayInputStream image2Bytes(BufferedImage bufferedImage) {
        ByteArrayOutputStream bos = null;
        try {
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.drawImage(bufferedImage, 0, 0, null);
            bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, OssConstant.THUMBNAIL_SUFFIX, bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(bos.toByteArray());
    }

    /**
     * 将指定图片按照参数裁剪到指定图片
     *
     * @param srcpath
     * @param x
     * @param y
     * @param width
     * @param height
     * @param subpath
     * @param suffix
     */
    public static void cut(String srcpath, int x, int y, int width, int height, String subpath, String suffix) {
        FileInputStream is = null;
        ImageInputStream iis = null;
        try {
            is = new FileInputStream(srcpath); //读取原始图片
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix); //ImageReader声称能够解码指定格式
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is); //获取图片流
            reader.setInput(iis, true); //将iis标记为true（只向前搜索）意味着包含在输入源中的图像将只按顺序读取
            ImageReadParam param = reader.getDefaultReadParam(); //指定如何在输入时从 Java Image I/O框架的上下文中的流转换一幅图像或一组图像
            Rectangle rect = new Rectangle(x, y, width, height); //定义空间中的一个区域
            param.setSourceRegion(rect); //提供一个 BufferedImage，将其用作解码像素数据的目标。
            BufferedImage bi = reader.read(0, param); //读取索引imageIndex指定的对象
            ImageIO.write(bi, suffix, new File(subpath));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(is);
            close(iis);
        }
    }

    /**
     * 将图片输入流按照参数裁剪后转为输出流
     *
     * @param is
     * @param x
     * @param y
     * @param width
     * @param height
     * @param suffix
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream getCutByte(InputStream is, int x, int y, int width, int height, String suffix) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageInputStream iis = null;
        try {
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix); //ImageReader声称能够解码指定格式
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is); //获取图片流
            reader.setInput(iis, true); //将iis标记为true（只向前搜索）意味着包含在输入源中的图像将只按顺序读取
            ImageReadParam param = reader.getDefaultReadParam(); //指定如何在输入时从 Java Image I/O框架的上下文中的流转换一幅图像或一组图像
            Rectangle rect = new Rectangle(x, y, width, height); //定义空间中的一个区域
            param.setSourceRegion(rect); //提供一个 BufferedImage，将其用作解码像素数据的目标。
            BufferedImage bi = reader.read(0, param); //读取索引imageIndex指定的对象
            ImageIO.write(bi, suffix, bos);
        } finally {
            close(is);
            close(iis);
        }
        return bos;
    }

    /**
     * 关闭流
     *
     * @param is
     */
    public static void close(Closeable is) {
        if (Objects.nonNull(is)) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 输入流转化为ImageIcon
     *
     * @param is
     * @return
     */
    public static ImageIcon stream2Image(InputStream is) {
        try {
            BufferedImage read = ImageIO.read(is);
            ImageIcon imageIcon = new ImageIcon(read);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            return imageIcon;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(is);
        }
        return null;
    }

    /**
     * 检验图片是否全黑，不全黑返回 BufferedImage 否则返回null
     *
     * @param is
     * @param isCheck
     * @return
     */
    public static BufferedImage checkBlackPicture(InputStream is, boolean isCheck) {
        BufferedImage read = null;
        try {
            read = ImageIO.read(is);
            Integer y1 = getPicY1(read);
            Integer y2 = getPicY2(read, y1 + boundary);
            int height = y2 - y1;

            Integer x1 = getPicX1(read);
            Integer x2 = getPicX2(read, x1 + boundary);
            int width = x2 - x1;

            if (height <= boundary * 2 && width <= boundary * 2 && isCheck) {
                return null;
            }
            log.info("图片坐标： x1 = {}, x2 = {},  y1 = {}, y2 ={}, 图片宽高： width = {}, height = {}", x1, x2, y1, y2,width,height);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(is);
        }
        return read;
    }


    /**
     * 将图片输入流裁剪黑边后转为输出流
     *
     * @param is
     * @return
     */
    public static ByteArrayInputStream dealPictureBlackBoundary(InputStream is, boolean isCheck) {
        ByteArrayOutputStream os = null;
        try {
            BufferedImage read = ImageIO.read(is);
            Integer y1 = getPicY1(read);
            Integer y2 = getPicY2(read, y1 + boundary);
            int height = y2 - y1;

            Integer x1 = getPicX1(read);
            Integer x2 = getPicX2(read, x1 + boundary);
            int width = x2 - x1;

            if (height <= boundary * 2 && width <= boundary * 2 ) {
                if (isCheck) {
                    return null;
                }else {
                    return new ByteArrayInputStream(is.readAllBytes());
                }
            }
            ByteArrayOutputStream ou = new ByteArrayOutputStream();
            int width1 = read.getWidth();
            int height1 = read.getHeight();
            int heightNum = height > height1 ? height1 : height;
            int widthNum = width > width1 ? width1 : width;

            ImageIO.write(read, OssConstant.THUMBNAIL_SUFFIX, ou);
            log.info("裁剪图片坐标： x1 = {}, x2 = {},  y1 = {}, y2 ={}, 裁剪图片宽高： width = {}, height = {} , BufferedImage： width1 = {} , height1 = {} ," +
                    " 最终尺寸 ：widthNum = {}， heightNum = {}", x1, x2, y1, y2, width, height, width1, height1, widthNum, heightNum);
            os = getCutByte(new ByteArrayInputStream(ou.toByteArray()), x1, y1, widthNum, heightNum, OssConstant.THUMBNAIL_SUFFIX);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(is);
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

    /**
     * 将BufferedImage 裁剪黑边后 转为输出流
     *
     * @param read
     * @return
     */
    public static ByteArrayInputStream dealBufferedImage(BufferedImage read) {
        ByteArrayOutputStream os = null;
        try {
            Integer y1 = getPicY1(read);
            Integer y2 = getPicY2(read, y1 + boundary);
            int height = y2 - y1;

            Integer x1 = getPicX1(read);
            Integer x2 = getPicX2(read, x1 + boundary);
            int width = x2 - x1;

            ByteArrayOutputStream ou = new ByteArrayOutputStream();
            ImageIO.write(read, OssConstant.THUMBNAIL_SUFFIX, ou);
            log.info("裁剪图片坐标： x1 = {}, x2 = {},  y1 = {}, y2 ={}, 裁剪图片宽高： width = {}, height = {}", x1, x2, y1, y2 ,width,height);
            os = getCutByte(new ByteArrayInputStream(ou.toByteArray()), x1, y1, width, height, OssConstant.THUMBNAIL_SUFFIX);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(os.toByteArray());
    }


    public static void main(String[] args) throws Exception {
        String path = "E:\\桌面\\图片\\黑边\\a13b3c7d-4385-4c2f-ad54-5bcbbeceb6a15-s.jpg";
        path = "E:\\桌面\\图片\\黑边\\8bd37bb7-dd10-40a4-b32f-11afc745dd06-s.jpg";//横向

       // path = "E:\\桌面\\图片\\黑边\\a13b3c7d-4385-4c2f-ad54-5bcbbeceb6a5-s.jpg";//竖向

        File file = new File(path);
        System.out.println(file.length());
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
        // BufferedImage read = ImageIO.read(new File(path));
        //BufferedImage read = ImageIO.read(is);
        // ImageIcon imageIcon = new ImageIcon(read);
        // ImageIcon imageIcon = stream2Image(is);

//        Integer y1 = getPicY1(read);
//        System.out.println("y1 起点 = " + y1);
//
//        Integer y2 = getPicY2(read, y1 + 10);
//        System.out.println("y2 起点 = " + y2);
//        int height = y2 - y1;
//        System.out.println("height  = " + height);
//
//        Integer x1 = getPicX1(read);
//        System.out.println("x1 起点 = " + x1);
//
//
//        Integer x2 = getPicX2(read, x1 + 10);
//        System.out.println("x2 起点 = " + x2);
//        int width = x2 - x1;
//        System.out.println("width  = " + width);
//
//        ByteArrayOutputStream ou = new ByteArrayOutputStream();
//        ImageIO.write(read, OssConstant.THUMBNAIL_SUFFIX, ou);
//        ByteArrayOutputStream jpg = getCutByte(new ByteArrayInputStream(ou.toByteArray()), x1, y1, width, height, OssConstant.THUMBNAIL_SUFFIX);

        ByteArrayInputStream byteArrayInputStream = dealPictureBlackBoundary(is,true);

        byte[] bytes = byteArrayInputStream.readAllBytes();

        FileOutputStream outputStream = new FileOutputStream("d:/b.jpg");
        outputStream.write(bytes);


        //    cut(path, x1, y1, width, height, "d:/test_111.png", OssConstant.THUMBNAIL_SUFFIX);

    }


}
