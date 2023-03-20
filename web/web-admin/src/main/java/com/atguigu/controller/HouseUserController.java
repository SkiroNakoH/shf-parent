package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/houseUser")
@PreAuthorize("hasAnyAuthority('house.editUser')")
@Controller
public class HouseUserController extends BaseController {

    private static final String PAGE_CREATE = "houseUser/create";
    private static final String PAGE_EDIT = "houseUser/edit";
    private static final String LIST_INDEX = "redirect:/house/";
    @DubboReference
    private HouseUserService houseUserService;

    /**
     * 跳转新增房东界面
     * @param houseId
     * @param model
     * @return
     */
    @RequestMapping("/create")
    public String create(Long houseId, Model model){
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(houseId);

        model.addAttribute("houseUser",houseUser);

        return PAGE_CREATE;
    }


    /**
     * 新增房东
     * @param houseUser
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(HouseUser houseUser,Model model){
        houseUserService.insert(houseUser);
        return successPage(model,"新增房东成功！");
    }

    /**
     * 跳转修改房东信息页面
     * @param id 房东id
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model){
        HouseUser houseUser = houseUserService.getById(id);
        model.addAttribute("houseUser",houseUser);
        return PAGE_EDIT;
    }

    /**
     * 修改房东信息
     * @param houseUser
     * @param model
     * @return
     */
    @PostMapping("/update")
    public String update(HouseUser houseUser,Model model){

        houseUserService.update(houseUser);
        return successPage(model,"修改房东信息成功!");
    }

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId,@PathVariable Long id){
        houseUserService.delete(id);
        return LIST_INDEX + houseId;
    }
}
