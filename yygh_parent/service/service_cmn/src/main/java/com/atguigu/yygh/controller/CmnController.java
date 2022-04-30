package com.atguigu.yygh.controller;

import com.atguigu.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.service.DictService;
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
