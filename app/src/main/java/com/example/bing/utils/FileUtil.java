package com.example.bing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 14:18
 * Author     Payne.
 * About      类描述：
 */
public class FileUtil {

    /**
     * 删除单个文件或空文件夹
     */
    public static boolean delFile(String path) {
        try {
            File file = new File(path);
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean delFile(File file) {
        try {
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除文件夹及其所有子文件
     */
    public static boolean delFolder(String path) {
        try {
            File file = new File(path);
            if (file.isFile() || file.listFiles().length == 0) {
                delFile(file);
            } else {
                for (File tem : file.listFiles()) {
                    delFolder(tem.getPath());
                }
            }
            delFile(file);
            return !file.exists();
        } catch (Exception e) {
            L.e(" FileUtil  delFolder ", e.toString());
            return false;
        }
    }

    public static boolean move(String fromPath, String toPath) {
        try {
            L.d("");
            File from = new File(fromPath);
            from.renameTo(new File(toPath));
        } finally {
            File to = new File(toPath);
            return to.exists();
        }
    }

    public static boolean copy(File from, File to) {

        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(from);
            output = new FileOutputStream(to);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {

            }
        }


    }

    public static boolean createFile(String path) {
        try {
            L.d("");
            File file = new File(path);
            if (file.exists()) {
                return true;
            }
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                boolean mk = parentFile.mkdirs();
                L.d("");
            }

            file.createNewFile();
            L.d("");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return new File(path).exists();
        }
    }


}
