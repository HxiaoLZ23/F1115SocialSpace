package com.form1115.crm.controller;

import com.form1115.crm.common.Result;
import com.form1115.crm.common.UploadBean;
import com.form1115.crm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadBean uploadBean;

    @Autowired
    private AccountService accountService;

    @PostMapping("/uploadFile.do")
    public Result uploadFile(MultipartFile headImg) throws IOException {
        if (headImg == null) {
            return new Result(-1, "上传文件不能为空");
        }
        String originalFilename = headImg.getOriginalFilename();
        if ("".equals(originalFilename)) {
            return new Result(-1, "上传文件不能为空");
        }

        // 可以上传头像了
        // 判断上传的目录是否存在
        String saveFilePath = uploadBean.getBaseUrl();
        File file = new File(saveFilePath);
        if(!file.exists()) {
            file.mkdirs();
        }

        // 根据上传的文件名称重新封装成File对象
        UUID uuid = UUID.randomUUID();
        // hello.png
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        File finalFile = new File(file+"/"+uuid+extName);
        headImg.transferTo(finalFile);

        //将图片名称更新到数据库中
        Result result = accountService.modifyAccountImgUrl(uuid + extName);
        result.setData(uuid+extName);


        return result;
    }
}
