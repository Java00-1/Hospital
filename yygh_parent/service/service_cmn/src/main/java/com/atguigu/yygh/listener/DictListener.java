package com.atguigu.yygh.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.yygh.mapper.DictMapper;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

public class DictListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        //将数据添加到数据库中
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
