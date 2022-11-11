package com.bnmotor.icv.tsp.device.util;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.google.zxing.client.j2se.MatrixToImageConfig.BLACK;
import static com.google.zxing.client.j2se.MatrixToImageConfig.WHITE;

@Slf4j
public class QRCodeUtils {
    private static final String imageFormat = "PNG";
    private static final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        return getQRCodeImage(text, width, height, null);
    }

    public static byte[] getQRCodeImage(String text, int width, int height, String logoPath) throws WriterException, IOException {
        Map<EncodeHintType, Object> defaultHints = createDefaultHints();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, defaultHints);
        byte[] pngData = null;
        if (StringUtils.isEmpty(logoPath)) {
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, pngOutputStream);
            pngData = pngOutputStream.toByteArray();
        } else {
            if (StringUtils.isNotEmpty(logoPath)) {
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        //循环将二维码内容填充至图片
                        image.setRGB(i, j, bitMatrix.get(i, j) ? BLACK : WHITE);
                    }
                }
                Graphics2D g = image.createGraphics();
                BufferedImage logo = getRemoteBufferedImage(logoPath);
                if (logo == null) {
                    throw new AdamException(RespCode.UNKNOWN_ERROR);
                }

                int widthLogo = Math.min(logo.getWidth(), image.getWidth() * 2 / 10);
                int heightLogo = Math.min(logo.getHeight(), image.getHeight() * 2 / 10);
                int x = (image.getWidth() - widthLogo) / 2;
                int y = (image.getHeight() - heightLogo) / 2;
                // 开始绘制图片
                g.drawImage(logo, x, y, widthLogo, heightLogo, null);
                g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
                //边框宽度
                g.setStroke(new BasicStroke(2));
                //边框颜色
                g.setColor(Color.WHITE);
                g.drawRect(x, y, widthLogo, heightLogo);
                g.dispose();
                logo.flush();
                image.flush();
                pngData = imageToBytes(image);
            }
        }
        return pngData;
    }

    /**
     * 删除白边
     */
    private static Map<EncodeHintType, Object> createDefaultHints() {
        // 设置编码字符集
        Map<EncodeHintType, Object> hints = new HashMap<>(8);
        //设置编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //设置容错率最高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置白边框，0 仍然有一点边框
        hints.put(EncodeHintType.MARGIN, 1);
        return hints;
    }


    /**
     * BufferedImage转byte[]
     *
     * @param bufferedImage BufferedImage对象
     * @return byte[]
     */
    private static byte[] imageToBytes(BufferedImage bufferedImage) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, imageFormat, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return out.toByteArray();
    }

    public static BufferedImage getRemoteBufferedImage(String imageURL) {
        URL url;
        InputStream is = null;
        BufferedImage bufferedImage;
        try {
            url = new URL(imageURL);
            is = url.openStream();
            bufferedImage = ImageIO.read(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.info("imageURL: " + imageURL + ",无效!");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("imageURL: " + imageURL + ",读取失败!");
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bufferedImage;
    }
}
