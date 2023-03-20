package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_INDEX = "redirect:/house";
    private static final String PAGE_DETAIL = "house/detail";
    private static final String PAGE_UPLOAD = "house/uploadCover";
    @DubboReference
    private HouseService houseService;

    @DubboReference
    private DictService dictService;

    @DubboReference
    private CommunityService communityService;

    @DubboReference
    private HouseImageService houseImageService;

    @DubboReference
    private HouseBrokerService houseBrokerService;

    @DubboReference
    private HouseUserService houseUserService;

    /**
     * 跳转房源首页,并带查询条件
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('house.show')")
    @RequestMapping
    public String index(@RequestParam Map filters, Model model) {
        model.addAttribute("filters", filters);

        saveAllDictToRequestScope(model);

        PageInfo<House> pageInfo = houseService.findPage(filters);
        model.addAttribute("page", pageInfo);
        return PAGE_INDEX;
    }

    /**
     * 跳转新增页面
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('house.create')")
    @RequestMapping("/create")
    public String create(Model model) {
        saveAllDictToRequestScope(model);
        return PAGE_CREATE;
    }

    /**
     * 新增房源
     *
     * @param house
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('house.create')")
    @PostMapping("/save")
    public String save(House house, Model model) {
        houseService.insert(house);
        return successPage(model, "新增房源成功!");
    }

    /**
     * 跳转修改房源页面，并回显数据
     *
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('house.edit')")
    @RequestMapping("/edit/{id}")
    private String edit(@PathVariable Long id, Model model) {

        House house = houseService.getById(id);
        model.addAttribute("house", house);
        saveAllDictToRequestScope(model);
        return PAGE_EDIT;
    }

    /**
     * 修改房源
     *
     * @param house
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('house.edit')")
    @PostMapping("/update")
    private String update(House house, Model model) {
        houseService.update(house);
        return successPage(model, "修改房源成功！！");
    }

    /**
     * 删除房源
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('house.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        houseService.delete(id);
        return LIST_INDEX;
    }

    /**
     * 发布/取消发布 房源
     * @param id
     * @param status
     * @return
     */
    @PreAuthorize("hasAnyAuthority('house.publish')")
    @RequestMapping("/publish/{id}/{status}")
    public String publish(@PathVariable Long id, @PathVariable Long status) {
        houseService.publish(id, status);
        return LIST_INDEX;
    }


    /**
     * 根据id，查看房源详情
     *
     * @param houseId hosue_id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('house.show')")
    @RequestMapping("/{id}")
    public String detail(@PathVariable("id") Long houseId, Model model) {
        //房源信息
        House house = houseService.getById(houseId);
        model.addAttribute("house", house);

        //小区信息
        Community community = communityService.getByHouseId(houseId);
        model.addAttribute("community", community);

        //房源图片 type=1
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(houseId, 1);
        model.addAttribute("houseImage1List", houseImage1List);

        //房产图片 type=2
        List<HouseImage> houseImage2List = houseImageService.findHouseImageList(houseId, 2);
        model.addAttribute("houseImage2List", houseImage2List);

        //经济人信息
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(houseId);
        model.addAttribute("houseBrokerList", houseBrokerList);

        //房东信息
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(houseId);
        model.addAttribute("houseUserList", houseUserList);

        return PAGE_DETAIL;
    }

    /**
     * 跳转上传图片页面
     *
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('house.editImage')")
    @RequestMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return PAGE_UPLOAD;
    }

    /**
     * 上传图片
     * @param id
     * @param file
     * @param model
     * @return
     * @throws IOException
     */
    @PreAuthorize("hasAnyAuthority('house.editImage')")
    @PostMapping("/upload/{id}")
    public String upload(@PathVariable Long id, @RequestParam MultipartFile file, Model model) throws IOException {
        //七牛云上传
        String fileName = UUID.randomUUID().toString().replace("-", "") +
                file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        QiniuUtils.upload2Qiniu(file.getBytes(), fileName);

        //数据库更新
        House house = new House();
        house.setId(id);
        house.setDefaultImageUrl(QiniuUtils.getUrlName(fileName));
        houseService.update(house);


        return successPage(model, "新增房源封面成功！");
    }


    /**
     * 遍历房屋信息,返回给前端页面
     *
     * @param model
     */
    private void saveAllDictToRequestScope(Model model) {
        List<Community> communityList = communityService.findAll();
        model.addAttribute("communityList", communityList);

        List<Dict> houseTypeList = dictService.findDictListByParentDictCode("houseType");
        model.addAttribute("houseTypeList", houseTypeList);

        List<Dict> floorList = dictService.findDictListByParentDictCode("floor");
        model.addAttribute("floorList", floorList);

        List<Dict> buildStructureList = dictService.findDictListByParentDictCode("buildStructure");
        model.addAttribute("buildStructureList", buildStructureList);

        List<Dict> directionList = dictService.findDictListByParentDictCode("direction");
        model.addAttribute("directionList", directionList);

        List<Dict> decorationList = dictService.findDictListByParentDictCode("decoration");
        model.addAttribute("decorationList", decorationList);

        List<Dict> houseUseList = dictService.findDictListByParentDictCode("houseUse");
        model.addAttribute("houseUseList", houseUseList);

    }

}
