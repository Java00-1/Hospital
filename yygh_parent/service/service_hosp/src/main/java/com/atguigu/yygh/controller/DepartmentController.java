package com.atguigu.yygh.controller;


import com.atguigu.result.Result;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("getDeptList/{hoscode}")
    //get department by hospital code
    public Result getDepartmentList(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }
}
