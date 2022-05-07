package com.atguigu.yygh.controller;

import com.atguigu.result.Result;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/hosp/hospital")
//@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //hospital list
    @GetMapping("list/{page}/{limit}")
    public Result hospitalList(@PathVariable Integer page, @PathVariable Integer limit, HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pageModel = hospitalService.selectHospitalPage(page,limit,hospitalQueryVo);
        List<Hospital> content = pageModel.getContent();
        long totalElements = pageModel.getTotalElements();
        return Result.ok(pageModel);
    }

    //update hospital
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateHospitalStatus(@PathVariable String id,@PathVariable Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    //show hospital detail
    @GetMapping("showHospitalDetail/{id}")
    public Result showHospitalDetail(@PathVariable String id){
        Map<String,Object> map = hospitalService.getHospitalById(id);
        return Result.ok(map);
    }
}
