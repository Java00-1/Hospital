package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.repository.DepartmentRepository;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String strMap = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(strMap,Department.class);

        //query department by hoscode and depcode
        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());


        if(departmentExist != null){
            //department exist update data
            department.setUpdateTime(departmentExist.getCreateTime());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }else{
            //department does not exist create
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            //save department
            departmentRepository.save(department);
        }

    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        //create Pageable obj
        Pageable pageable = PageRequest.of(page-1 , limit);

        //convert departmentVo to department
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);

        //create Example obj
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department,matcher);

        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null){
            //mongodb operation
            departmentRepository.deleteById(department.getId());
        }
    }
}
