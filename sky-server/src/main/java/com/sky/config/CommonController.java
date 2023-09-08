package com.sky.config;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@ApiOperation("通用接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传",file.getName());
        //
        //原始文件名
        String originalFilename=file.getOriginalFilename();
        String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename= UUID.randomUUID().toString()+extension;
        try {
            String filepath =aliOssUtil.upload(file.getBytes(),filename);
            log.debug("...............",filepath);
            return  Result.success(filepath);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传失败");
        }
        return  Result.error(MessageConstant.UPLOAD_FAILED);
    }

}
