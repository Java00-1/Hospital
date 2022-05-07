package com.atguigu.yygh.controller;

import com.atguigu.result.Result;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitals = hospitalService.selectHospitalPage(page, limit, hospitalQueryVo);
        List<Hospital> content = hospitals.getContent();
        int totalPages = hospitals.getTotalPages();
        return Result.ok(hospitals);
    }

    @GetMapping("findHospByHosName/{hosname}")
    public Result findHospByHosName(@PathVariable String hosname){
        List<Hospital> list = hospitalService.findHospByHosname(hosname);
        return Result.ok(list);
    }

    //根据医院编号获取科室信息
    @GetMapping("department/{hoscode}")
    public Result getDepartmentByHoscode(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }


    //根据医院编号获取医院挂号详情信息
    @GetMapping("deptDetail/{hoscode}")
    public Result findDeptDetail(@PathVariable String hoscode){
        Map<String,Object> map = hospitalService.deptDetail(hoscode);
        return Result.ok(map);
    }

}
