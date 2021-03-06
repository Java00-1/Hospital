package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.repository.DepartmentRepository;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;

import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //create list to encapsulate data
        List<DepartmentVo> list = new ArrayList<>();

        //get all department data by hoscode
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        List<Department> departmentList = departmentRepository.findAll(example);

        //??????????????????????????? ????????????????????????????????????
        Map<String, List<Department>> departmentMap = departmentList.stream()
                .collect(Collectors.groupingBy(Department::getBigcode));

        //display map
        for(Map.Entry<String,List<Department>> entry : departmentMap.entrySet()){
            String bigCode = entry.getKey();
            List<Department> departmentListValue = entry.getValue();

            //encapsulate big department
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(departmentListValue.get(0).getBigname());

            //encapsulate small dept
            List<DepartmentVo> childrenList = new ArrayList<>();
            for (Department department : departmentListValue){
                DepartmentVo vo = new DepartmentVo();
                vo.setDepcode(department.getDepcode());
                vo.setDepname(department.getDepname());
                childrenList.add(vo);
            }

            departmentVo.setChildren(childrenList);
            list.add(departmentVo);
        }

        return list;
    }

    @Override
    public String getDeptName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null){
            return department.getDepname();
        }
        return null;
    }
}
