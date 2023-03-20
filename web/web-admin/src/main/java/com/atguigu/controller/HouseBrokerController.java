package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@PreAuthorize("hasAnyAuthority('house.editBroker')")
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {

    private static final String PAGE_INDEX = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";
    private static final String LIST_INDEX = "redirect:/house/";

    @DubboReference
    private AdminService adminService;

    @DubboReference
    private HouseBrokerService houseBrokerService;

    private void saveAdminList2Model(Model model) {
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList", adminList);
    }

    /**
     * 跳转新增经纪人页面
     *
     * @param houseId
     * @param model
     * @return
     */
    @GetMapping("/create")
    public String create(Long houseId, Model model) {
        saveAdminList2Model(model);

        HouseBroker houseBroker = new HouseBroker();
        houseBroker.setHouseId(houseId);

        model.addAttribute("houseBroker", houseBroker);
        return PAGE_INDEX;
    }

    /**
     * 保存新增经纪人
     * @param houseBroker
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(HouseBroker houseBroker, Model model) {
        Admin admin = adminService.getById(houseBroker.getBrokerId());

        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());

        houseBrokerService.insert(houseBroker);

        return successPage(model, "新增经纪人成功!");
    }

    /**
     * 跳转修改经纪人界面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        saveAdminList2Model(model);

        HouseBroker houseBroker = houseBrokerService.getById(id);
        model.addAttribute("houseBroker",houseBroker);

        return PAGE_EDIT;
    }

    /**
     * 修改经纪人
     * @param id    老经纪人id
     * @param houseBroker 新经纪人 houseBroker
     * @param model
     * @return
     */
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,HouseBroker houseBroker,Model model){
        houseBroker.setId(id);

        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());

        houseBrokerService.update(houseBroker);

        return successPage(model,"修改经纪人成功!!!");
    }

    /**
     * 删除经纪人
     * @param houseId   跳转回此页面需要房屋id
     * @param id
     * @return
     */
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id){
        houseBrokerService.delete(id);
        return LIST_INDEX + houseId;
    }

}
