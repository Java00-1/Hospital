package com.atguigu.yygh.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.listener.DictListener;
import com.atguigu.yygh.mapper.DictMapper;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.service.DictService;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        for (Dict dict : dictList){
            Long dictId = dict.getId();
            boolean hasChild = hasChild(dictId);
            dict.setHasChildren(hasChild);
        }
        return dictList;
    }

    @Override
    public void exportDictData(HttpServletResponse response) {
        //set download message
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition","attachment:fileName="+fileName+".xlsx");
        //query dict data
        List<Dict> list = baseMapper.selectList(null);
        ArrayList<DictEeVo> dictVoList = new ArrayList<>();
        for (Dict dict : list){
            DictEeVo dictEeVo = new DictEeVo();
            //将dict数据赋值转换给dictEeVo
            BeanUtils.copyProperties(dict,dictEeVo);
            dictVoList.add(dictEeVo);
        }
        try {
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("dict")
                    .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(value = "dict",allEntries = true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(dictMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断是否有子数据
    private boolean hasChild(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

}
