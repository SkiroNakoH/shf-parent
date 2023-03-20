package com.atguigu.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import com.atguigu.mapper.DictMapper;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.util.StringUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public BaseMapper<Dict> getBaseMapper() {
        return dictMapper;
    }

    @Override
    public List<Map<String, Object>> findZondes(Long parentId) {
        List<Dict> nodesList = findZondesByParentId(parentId);

        List<Map<String, Object>> dictList = new ArrayList<>();
        for (Dict node : nodesList) {
            Map<String, Object> map = new HashMap<>();

            map.put("id", node.getId());
            map.put("name", node.getName());
            map.put("isParent", findZondesByParentId(node.getId()).size() > 0);

            dictList.add(map);
        }

        return dictList;
    }

    @Override
    public List<Dict> findDictListByParentDictCode(String dictCode) {
        Jedis jedis = null;
        List<Dict> dictList;
        try {
            jedis = jedisPool.getResource();

            //redis中取值
            String value = jedis.get("findDictList:dict:dictCode:" + dictCode);
            if (!StringUtil.isEmpty(value)) {
                dictList  = JSON.parseArray(value, Dict.class);
//                dictList = JSON.parseObject(value, List.class);
            } else {
                dictList = dictMapper.findDictListByParentDictCode(dictCode);
                //存入redis中
                jedis.set("findDictList:dict:dictCode:" + dictCode, JSON.toJSONString(dictList));
            }
        } finally {
            jedis.close();
            if (jedis.isConnected()) {
                jedis.disconnect();
            }
            if (jedis != null) {
                jedis = null;
            }
        }

        return dictList;
    }

    @Override
    public List<Dict> findZondesByParentId(Long parentId) {
        Jedis jedis = null;
        List<Dict> dictList;
        try {
            jedis = jedisPool.getResource();
            dictList = null;

            String value = jedis.get("findZondes:dict:parentId:" + parentId);
            if (!StringUtil.isEmpty(value)) {
                //redis中有findZondesByParentId的值
                dictList = JSON.parseArray(value, Dict.class);
            } else {
                //redis没findZondesByParentId的值
                dictList = dictMapper.findZondesByParentId(parentId);
                jedis.set("findZondes:dict:parentId:" + parentId, JSON.toJSONString(dictList));
            }
        } finally {
            jedis.close();
            if (jedis.isConnected()) {
                jedis.disconnect();
            }
            if (jedis != null) {
                jedis = null;
            }
        }

        return dictList;

    }

    @Override
    public List<Dict> findDictListByParentId(Long parentId) {
        Jedis jedis = null;
        List<Dict> dictList;
        try {
            jedis = jedisPool.getResource();
            //从redis中取值
            String value = jedis.get("findDictList:dict:parentId:" + parentId);
            if (!StringUtil.isEmpty(value)) {
                dictList = JSON.parseArray(value, Dict.class);
            }else{
               dictList = dictMapper.findDictListByParentId(parentId);
               //存入redis中
               jedis.set("findDictList:dict:parentId:" + parentId,JSON.toJSONString(dictList));
            }
        } finally {
            jedis.close();
            if (jedis.isConnected()) {
                jedis.disconnect();
            }
            if (jedis != null) {
                jedis = null;
            }
        }

        return dictList;
    }


}
