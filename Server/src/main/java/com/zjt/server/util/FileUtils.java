package com.zjt.server.util;

import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class FileUtils {

    public boolean downloadFile(byte[] data, String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            // 创建临时文件
            File tempFile = File.createTempFile(fileName, ".tmp");

            // 将byte[]数组写入临时文件
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(data);
            }

            fileName = URLEncoder.encode(fileName,"UTF-8");

            // 设置响应头，告诉浏览器返回的是一个文件
            // 根据文件扩展名设置Content-Type
            String contentType = "application/octet-stream"; // 默认为通用的二进制文件类型
            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 获取响应输出流
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                // 将文件发送给客户端
                java.nio.file.Files.copy(tempFile.toPath(), outputStream);
                outputStream.flush();
            }

            // 删除临时文件
            tempFile.delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
            // 处理异常
        }
    }
}
