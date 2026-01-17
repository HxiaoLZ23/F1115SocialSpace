package com.form1115.crm.controller;

import com.form1115.crm.common.UploadBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    private UploadBean uploadBean;



    @RequestMapping("/downloadFile.do")
    public void downloadFile(String imgUrl, HttpServletResponse response) throws IOException {
        // 思路，将服务器上的图片 通过输出流的方式写出去
        // 输入流读取图片===>输出流写图片
        String saveFilePath = uploadBean.getBaseUrl();
        File file = new File(saveFilePath, imgUrl);
        
        try (FileInputStream fis = new FileInputStream(file);
             ServletOutputStream os = response.getOutputStream()) {
            int len = -1;
            byte[] bytes = new byte[1024];
            while ((len = fis.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        }
    }
}
