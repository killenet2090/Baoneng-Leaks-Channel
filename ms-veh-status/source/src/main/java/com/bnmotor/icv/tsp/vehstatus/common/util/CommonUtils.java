package com.bnmotor.icv.tsp.vehstatus.common.util;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringJoiner;

/**
 * @ClassName: CommonUtils
 * @Description: 公共utils
 * @author: huangyun1
 * @date: 2020/6/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class CommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 连接字符串
     * @param joinChar
     * @param args
     * @return
     */
    public static String appendString(String joinChar, String... args) {
        StringJoiner joiner = new StringJoiner(joinChar);
        for(String arg : args) {
            joiner.add(arg);
        }
        return joiner.toString();
    }

    /**
     * 根据包名获取类
     * @param packageName
     * @return
     * @throws Exception
     */
    public static List<Class> getClasses(String packageName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            // 用'/'代替'.'路径
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
            ArrayList<Class> classes = new ArrayList<Class>();
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
            return classes;
        } catch (Exception e) {
            logger.error("根据包名获取类发生异常", e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 根据目录路径和包名获取类
     * @param directory
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                // 去掉'.class'
                Class clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if(ColumnPack.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
}
