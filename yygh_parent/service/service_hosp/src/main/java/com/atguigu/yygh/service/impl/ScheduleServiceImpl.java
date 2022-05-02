package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.repository.ScheduleRepository;
import com.atguigu.yygh.service.ScheduleService;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String strMap = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(strMap,Schedule.class);

        //query department by hoscode and depcode
        Schedule scheduleExist = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());

        if (scheduleExist != null){
            schedule.setUpdateTime(scheduleExist.getUpdateTime());
            schedule.setIsDeleted(0);
            schedule.setStatus(scheduleExist.getStatus());
            scheduleRepository.save(schedule);
        }else{
            schedule.setUpdateTime(new Date());
            schedule.setCreateTime(new Date());
            schedule.setStatus(1); // 可就诊
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        //create Pageable obj
        Pageable pageable = PageRequest.of(page-1 , limit);

        //convert departmentVo to department
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        schedule.setIsDeleted(0);
        schedule.setStatus(1);

        //create Example obj
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule,matcher);

        Page<Schedule> all = scheduleRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule != null){
            scheduleRepository.deleteById(schedule.getId());
        }
    }
}
