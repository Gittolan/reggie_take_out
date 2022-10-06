package com.itheima.reggie.controller;


import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //file是一个临时文件，需要转存到指定的位置，否则本次请求完将被删除
        log.info(file.toString());

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID().toString()+suffix;

        file.transferTo(new File(basePath+fileName));
        return R.success(fileName);
    }


    @GetMapping("download")
    public void download(String name, HttpServletResponse response) throws Exception {
        //输入流，通输入流读取文件
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

        //输出流，通过输出流将文件写回浏览器
        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("image/jpeg ");

        int len =0;
        byte[] bytes = new byte[1024];
        while((len = fileInputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,len);
        }
        outputStream.close();
        fileInputStream.close();

    }

//    @GetMapping("/download")
//    public void download(String name, HttpServletResponse response){
//
//        try {
//            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
//
//            ServletOutputStream outputStream = response.getOutputStream();
//
//            response.setContentType("imag/jpeg");
//
//            int len = 0;
//            byte[] bytes = new byte[1024];
//            while ( (len = fileInputStream.read(bytes))!=-1){
//
//                outputStream.write(bytes,0,len);
//                outputStream.flush();
//            }
//            outputStream.close();
//            fileInputStream.close();
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }




}
