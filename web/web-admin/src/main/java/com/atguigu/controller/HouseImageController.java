package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController extends BaseController {
    private static final String LIST_INDEX = "redirect:/house/";
    @DubboReference
    private HouseImageService houseImageService;

    private static final String PAGE_UPLOAD = "house/upload";

    /**
     * 跳转上传房屋相关图片页面
     *
     * @param houseId 房源id
     * @param type    房子类型
     * @param model
     * @return
     */
    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable Long houseId, @PathVariable Integer type, Model model) {

        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);
        return PAGE_UPLOAD;
    }

    /**
     * 上传房屋图片
     * @param houseId
     * @param type
     * @param files
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable Long houseId, @PathVariable Integer type,
                         @RequestParam(value = "file") MultipartFile[] files, Model model) {
        try {
            if (files.length > 0) {
                for (MultipartFile file : files) {

                    String newFileName = UUID.randomUUID().toString().replace("-", "") + "." +
                            file.getOriginalFilename().split("\\.")[1];


                    QiniuUtils.upload2Qiniu(file.getBytes(), newFileName);

                    HouseImage houseImage = new HouseImage();

                    houseImage.setHouseId(houseId);
                    houseImage.setImageName(newFileName);
                    houseImage.setImageUrl(QiniuUtils.getUrlName(newFileName));
                    houseImage.setType(type);

                    houseImageService.insert(houseImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    /**
     * 删除房屋图片
     * @param houseId
     * @param id
     * @return
     */
    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId,@PathVariable Long id){
        String fileName = houseImageService.getById(id).getImageName();
        QiniuUtils.deleteFileFromQiniu(fileName);

        houseImageService.delete(id);
        return LIST_INDEX + houseId;
    }
    

}
