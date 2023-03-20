package com.atguigu.controller;

import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/dict")
public class DictController {
    private static final String PAGE_INDEX = "dict/index";

    @DubboReference
    private DictService dictService;

    @RequestMapping
    public String index(){
        return PAGE_INDEX;
    }

    /**
     * 加载数据字典
     * @param parentId
     * @return
     */
    @ResponseBody
    @GetMapping("/findZnodes")
    public Result findZnodes(@RequestParam(name = "id",defaultValue = "0") Long parentId){
        List<Map<String, Object>> dictList =  dictService.findZondes(parentId);
        return Result.ok(dictList);
    }

    /**
     * 加载对应区域内部的模板
     * @param areaId
     * @return
     */
    @ResponseBody
    @GetMapping("/findDictListByParentId/{areaId}")
    public Result findDictListByParentId(@PathVariable("areaId") Long areaId){
        List<Dict> plateList = dictService.findZondesByParentId(areaId);

        return Result.ok(plateList);
    }
}
