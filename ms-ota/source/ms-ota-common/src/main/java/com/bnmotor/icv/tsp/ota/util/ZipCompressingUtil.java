package com.bnmotor.icv.tsp.ota.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 将目录下的多个文件打成一个*.ZIP文件
 */
/**
 * @ClassName: ZipCompressing.java ZipCompressing
 * @Description:
 * @author E.YanLonG
 * @since 2020-10-22 15:48:33
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public final class ZipCompressingUtil {
	private ZipCompressingUtil(){}

	/**
	 *
	 * @param file 输入的文件夹
	 * @param outputstream  输出ZIP文件
	 */
	public static void zipFileChannel(File file, OutputStream outputstream) {

		// 开始时间
		long starttime = System.currentTimeMillis();

		try (ZipOutputStream zipOut = new ZipOutputStream(outputstream);
				WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {

			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					try (FileInputStream fis = new FileInputStream(f); FileChannel fileChannel = fis.getChannel()) {
						zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + f.getName()));
						fileChannel.transferTo(0, fileChannel.size(), writableByteChannel);
					}

				}
			}

			long millis = System.currentTimeMillis() - starttime;

			log.info("zip cost millis|{}", millis);
		} catch (Exception e) {
			log.error("压缩文件处理失败.file.getName={}, file.getAbsolutePath={}", file.getName(), file.getAbsolutePath(),  e);
		}
	}
}