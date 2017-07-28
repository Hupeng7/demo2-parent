package cn.demo2.manage.controller;

import cn.demo2.common.vo.PicUploadResult;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/7/26.
 */
@Controller
public class FileUploadController {
    //引入日志文件  架构师可能会将日志对象进行包装   静态调用
    private static final Logger logger = Logger.getLogger(FileUploadController.class);

    @RequestMapping("/pic/upload")
    @ResponseBody
    public PicUploadResult fileUpload(MultipartFile uploadFile){
        PicUploadResult picUpload = new PicUploadResult();

        //1.获取下载文件名称
        String fileName = uploadFile.getOriginalFilename();

        //2.获取后缀名
        String endName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        //3.判断是否为图片格式
        if(!endName.matches("^.*(png|jpg|gif)$")){
            picUpload.setError(1);
            logger.error("~~~~~~~~~~~文件后缀名不是图片格式");
            return picUpload;
        }

        //4.判断文件是否是一个正确的图片
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            //获取文件width height否则会报错
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            picUpload.setWidth(width+"");
            picUpload.setHeight(height+"");

            String localPath = "E:/1704/JT/images/";
            String datePath = new SimpleDateFormat("yyyy/MM/dd/HH/mm").format(new Date());
            String urlPath = "http://image.jt.com/images/";

            localPath += datePath+"/"+fileName;
            urlPath += datePath+"/"+fileName;

            File file = new File(localPath);
            //判断文件夹是否存在
            if(!file.exists()){
                file.mkdirs();         //创建多个文件夹
            }

            //将文件写入
            uploadFile.transferTo(file);
            picUpload.setUrl(urlPath);
            logger.info("~~~~~~~~~~~文件写入成功"+localPath);

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("~~~~~~~~~~该文件是一个非法的文件");
            picUpload.setError(1);
            return picUpload;
        }

        return picUpload;


    }

}
