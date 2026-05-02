package com.wenkrang.faClip.Helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * ZIP 文件操作帮助类
 * 提供 ZIP 文件检测、解压等功能
 */
public class ZipHelper {
    // ZIP 文件标准文件头标识字节数组（PK 签名）
    private static final byte[] ZIP_HEADER = {0x50, 0x4B, 0x03, 0x04};

    /**
     * 判断指定文件是否为ZIP格式文件
     * 通过读取文件前4个字节与ZIP标准文件头进行比较来判断
     *
     * @param file 待检测的文件对象，可以为null
     * @return 如果文件存在且为ZIP格式则返回true，否则返回false
     */
    public static boolean isZipFile(@Nullable File file) {
        if (file == null || !file.isFile()) return false;

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[4];
            if (fis.read(header) == 4) {
                return Arrays.equals(header, ZIP_HEADER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解压zip文件到指定目录。
     *
     * @param file zip文件
     * @param Dir  解压目录
     * @throws IOException 抛出IO异常
     */
    public static void unzip(@NotNull File file, File Dir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                File newFile = new File(Dir, zipEntry.getName());
                if (!newFile.exists()) {
                    if (zipEntry.isDirectory()) {
                        newFile.mkdirs();
                    } else {
                        newFile.createNewFile();

                        try (FileOutputStream fo = new FileOutputStream(newFile)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = (zis.read(buffer))) != -1) {
                                fo.write(buffer, 0, length);
                            }
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

}
