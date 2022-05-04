package com.atguigu.yygh.controller;

import com.atguigu.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin //implement cross origin
public class CmnController {

    @Autowired
    private DictService dictService;

    //get subordinate node by dictCode
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    //query data by dictcode and value
    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,@PathVariable String value){
        String dictName = dictService.getDictName(dictCode,value);
        return dictName;
    }

    //query by value
    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value){
        String dictName = dictService.getDictName("",value);
        return dictName;
    }

    //根据数据id查询子数据列表
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    //导出数据字典接口
    @GetMapping("exportDict")
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
        //return Result.ok();
    }

    //导入数据词典
    @PostMapping("importDict")
    //MultipartFile file 用于文件上传
    public Result importDict(MultipartFile file){
        dictService.importDictData(file);

        return Result.ok();
    }


}
