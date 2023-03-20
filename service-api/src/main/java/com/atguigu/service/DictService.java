package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService extends BaseService<Dict> {
    List<Map<String, Object>> findZondes(Long parentId);

    List<Dict> findDictListByParentDictCode(String dictCode);

    List<Dict> findZondesByParentId(Long parentId);

    List<Dict> findDictListByParentId(Long parentId);
}
