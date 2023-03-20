package com.atguigu.controller;

import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/dict")
public class DictController {

    @DubboReference
    private DictService dictService;

    /**
     * 根据父亲名称加载页面上方条件搜索列表
     * @param dictCode 父节点名称 parent的DictCode
     * @return
     */
    @ResponseBody
    @GetMapping("/findDictListByParentDictCode/{dictCode}")
    public Result findDictListByParentDictCode(@PathVariable String dictCode){
        List<Dict> dictList = dictService.findDictListByParentDictCode(dictCode);
        return Result.ok(dictList);
    }

    /**
     * 根据区域id查找 板块信息
     * @param parentId 区域id area_id
     * @return
     */
    @ResponseBody
    @GetMapping("/findDictListByParentId/{id}")
    public Result findDictListByParentId(@PathVariable("id") Long parentId){
        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return Result.ok(dictList);
    }

}
