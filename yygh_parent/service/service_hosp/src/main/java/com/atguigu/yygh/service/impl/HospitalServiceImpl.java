package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.client.DictFeignClient;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.repository.HospitalRepository;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;// ? add dependency service_cmn_client

    @Override
    public void save(Map<String, Object> paramMap) {
        //set map as json obj
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);
        //insert data into mongodb
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);
        //judge data if exist
        if(hospitalExist != null){
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(hospitalExist.getUpdateTime());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else{
        //not exist,insert
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

        //exist,update
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospitalPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //pageable obj
        Pageable pageable = PageRequest.of(page-1,limit);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);

        //Example obj
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Example<Hospital> example=Example.of(hospital,matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        //use feign obj to query

        //display list to get hospital level and encapsulate it
        pages.getContent().stream().forEach(item ->{
            this.setHospitalHosType(item);
        });

        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        //get wanted to be update  hospital
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String,Object> getHospitalById(String id) {
        Map<String, Object> map = new HashMap<>();

        Hospital hospital = hospitalRepository.findById(id).get();

        this.setHospitalHosType(hospital);

        map.put("hospital",hospital);
        map.put("bookingRule",hospital.getBookingRule());
        hospital.setBookingRule(null);

        return map;
    }

    @Override
    public String getHospitalName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital != null){
            return hospital.getHosname();
        }
        return null;
    }

    private Hospital setHospitalHosType(Hospital hospital) {
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        String province = dictFeignClient.getName(hospital.getProvinceCode());
        String city = dictFeignClient.getName(hospital.getCityCode());
        String district = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("hosTypeString",hostypeString);
        hospital.getParam().put("address",province+city+district);

        return hospital;
    }
}
