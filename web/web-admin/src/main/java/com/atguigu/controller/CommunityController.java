package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequestMapping("/community")
@Controller
public class CommunityController extends BaseController {

    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private static final String LIST_INDEX = "redirect:/community";
    @DubboReference
    private CommunityService communityService;
    @DubboReference
    private DictService dictService;

    /**
     * 小区管理首页
     * @param filters
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('community.show')")
    @RequestMapping
    public String index(@RequestParam Map filters, Model model){

        if(!filters.containsKey("areaId"))
            filters.put("areaId","");
        if(!filters.containsKey("plateId"))
            filters.put("plateId","");

        model.addAttribute("filters",filters);

        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");

        model.addAttribute("areaList",areaList);

        PageInfo<Community> pageInfo = communityService.findPage(filters);

        model.addAttribute("page",pageInfo);

        return PAGE_INDEX;
    }

    /**
     * 跳转新增页面
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('community.create')")
    @RequestMapping("/create")
    public String create(Model model){

        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList",areaList);
        return PAGE_CREATE;
    }

    /**
     * 新增小区
     * @param community
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('community.create')")
    @PostMapping("/save")
    public String save(Community community,Model model){
        communityService.insert(community);

        return successPage(model,"新增小区信息成功!!");
    }

    /**
     * 跳转修改页面
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('community.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model){
        Community community = communityService.getById(id);
        model.addAttribute("community",community);
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList",areaList);
        return PAGE_EDIT;
    }

    /**
     * 修改小区信息
     * @param community
     * @return
     */
    @PreAuthorize("hasAnyAuthority('community.edit')")
    @RequestMapping("/update")
    public String update(Community community,Model model){
        communityService.update(community);
        return successPage(model,"修改小区信息成功!");
    }

    /**
     * 删除小区
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('community.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        communityService.delete(id);
        return LIST_INDEX;
    }
}
