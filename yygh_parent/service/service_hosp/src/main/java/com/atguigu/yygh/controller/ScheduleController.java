package com.atguigu.yygh.controller;

import com.atguigu.result.Result;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
//@CrossOrigin
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    //根据医院编号和科室编号进行排班数据查询
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable Integer page,
                                  @PathVariable Integer limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){
        Map<String,Object> map = scheduleService.getScheduleRule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    //根据医院编号,科室编号和工作日期进行排班详细信息的查询
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}
