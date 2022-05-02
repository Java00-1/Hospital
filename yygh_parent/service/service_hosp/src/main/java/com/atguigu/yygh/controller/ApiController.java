package com.atguigu.yygh.controller;

import com.atguigu.exception.YyghException;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.yygh.HttpRequestHelper;
import com.atguigu.yygh.MD5;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.service.HospitalSetService;
import com.atguigu.yygh.service.ScheduleService;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    //remove schedule interface
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        //TODO

        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }

    //query schedule interface
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) paramMap.get("hoscode");

        // current page
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        // show data each page
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String)paramMap.get("limit"));

        // TODO check sign

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        // departmentQueryVo : encapsulate query condition
        scheduleQueryVo.setHoscode(hoscode);

        //invoke service method
        Page<Schedule> scheduleModel = scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
        return Result.ok(scheduleModel);
    }

    //upload schedule interface
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //TODO check sign
        scheduleService.save(paramMap);
        return Result.ok();

    }

    //remove department interface
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        //TODO check sign

        //remove department by hoscode and depcode
        departmentService.remove(hoscode,depcode);

        return Result.ok();
    }

    //query department interface
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) paramMap.get("hoscode");
        // current page
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        // show data each page
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String)paramMap.get("limit"));

        // TODO check sign

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        // departmentQueryVo : encapsulate query condition
        departmentQueryVo.setHoscode(hoscode);

        //invoke service method
        Page<Department> pageModel = departmentService.findPageDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }

    //upload department interface
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //1 get signKey from hospital system
        String hospitalSign = (String) paramMap.get("sign");

        //2 get hospitalSet signKey data by hoscode
        String hoscode = (String)paramMap.get("hoscode");
        String signKey  = hospitalSetService.getSignKey(hoscode);

        //3 md5 encrypt
        //String encryptSign = MD5.encrypt(signKey);
        String encryptSign = MD5.encrypt(signKey);

        //4 judge signKey whether equal
        if(!hospitalSign.equals(encryptSign)){
            //if(!hospitalSign.equals(signKey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //invoke service method
        departmentService.save(paramMap);
        return Result.ok();
    }

    //query hospital
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //1 get signKey from hospital system
        String hospitalSign = (String) paramMap.get("sign");

        //2 get hospitalSet signKey data by hoscode
        String hoscode = (String)paramMap.get("hoscode");
        String signKey  = hospitalSetService.getSignKey(hoscode);

        //3 md5 encrypt
        //String encryptSign = MD5.encrypt(signKey);
        String encryptSign = MD5.encrypt(signKey);

        //4 judge signKey whether equal
        if(!hospitalSign.equals(encryptSign)){
            //if(!hospitalSign.equals(signKey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //invoke method to get hospital by hoscode
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    //upload hospital interface
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request){
        //get data from hospital
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //1 get signKey from hospital system
        String hospitalSign = (String) paramMap.get("sign");

        //2 get hospitalSet signKey data by hoscode
        String hoscode = (String)paramMap.get("hoscode");
        String signKey  = hospitalSetService.getSignKey(hoscode);

        //3 md5 encrypt
        //String encryptSign = MD5.encrypt(signKey);
        String encryptSign = MD5.encrypt(signKey);

        //4 judge signKey whether equal
        if(!hospitalSign.equals(encryptSign)){
        //if(!hospitalSign.equals(signKey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //5 "" convert to +
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData); //key value

        //invoke service
        hospitalService.save(paramMap);
        return Result.ok();
    }
}
