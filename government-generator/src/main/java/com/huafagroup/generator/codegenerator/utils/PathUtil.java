package com.huafagroup.generator.codegenerator.utils;

import com.huafagroup.generator.CoreApplication;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class PathUtil {

    public static String getProjectPath() {

        java.net.URL url = CoreApplication.class.getProtectionDomain().getCodeSource().getLocation();

        String filePath = null;
        try {
            filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
            filePath = filePath.substring(1, filePath.length() - ("/target/classes/").length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(filePath);
        return filePath;

    }

    public static String getCurrentPath() {

        Class<?> caller = getCaller();
        if (caller == null) {
            caller = PathUtil.class;
        }

        return getCurrentPath(caller);
    }


    public static Class<?> getCaller() {
        StackTraceElement[] stack = (new Throwable()).getStackTrace();
        if (stack.length < 3) {
            return PathUtil.class;
        }
        String className = stack[2].getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentPath(Class<?> cls) {
        String path = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.replaceFirst("file:/", "");
        path = path.replaceAll("!/", "");
        if (path.lastIndexOf(File.separator) >= 0) {
            path = path.substring(0, path.lastIndexOf(File.separator));
        }
        if ("/".equalsIgnoreCase(path.substring(0, 1))) {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("window")) {
                path = path.substring(1);
            }
        }
        return path;
    }
}
