package com.atguigu.yygh.controller;

import com.atguigu.exception.YyghException;
import com.atguigu.result.Result;
import com.atguigu.yygh.MD5;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.service.HospitalSetService;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/*http://localhost/admin/hosp/hospitalSet/findPageHospSet/1/3
                /admin/hosp/hospitalSet/findPageHospSet/{current}/{limit}/*/
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
//@CrossOrigin(allowCredentials = "true")
public class HospitalSetController {

    @Autowired //set value by type
    private HospitalSetService hospitalSetService;

   /* @GetMapping("findAll")
    public List<HospitalSet> findAll(){
        List<HospitalSet> list = hospitalSetService.list();//query all message
        return list;
    }*/
   @GetMapping("findAll")
   /*医院信息查询接口*/
   public Result findAll(){
       List<HospitalSet> list = hospitalSetService.list();//query all message
       return Result.ok(list);

   }

    @DeleteMapping("{id}")
    /**
     * restful
     */
    /*医院信息删除接口*/
    public Result removeHosp(@PathVariable long id){
        boolean flag = hospitalSetService.removeById(id);
        if (flag){
            return Result.ok();
        }else{
            return Result.fail();
        }

    }

    /*分页条件查询*/

    @PostMapping("findPageHospSet/{current}/{limit}")
    /**
     * current:当前页数
     * limit:显示页数
     * hospitalSetQueryVo:保存查询条件的类
     */
    public Result findPageHospSet(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false)/*表示该参数可以为null*/
                                          HospitalSetQueryVo hospitalSetQueryVo){
        //新建Page对象
        Page<HospitalSet> page = new Page<>(current, limit);

        //构造查询条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();

        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)){
            //模糊查询
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hoscode)){
            //等值查询
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }

        //调用方法实现分页查询
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page,wrapper);

        //返回结果
        return Result.ok(hospitalSetPage);
    }

    /*添加医院设置*/
    @PostMapping("saveHospSet")
    public Result saveHospSet(@RequestBody HospitalSet hospitalSet){
        //设置状态1可用 0不可用
        hospitalSet.setStatus(1);

        //签名秘钥
        Random random = new Random();
        //MD5加密
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

        //调用service
        boolean save = hospitalSetService.save(hospitalSet);
        if (save){
            return Result.ok();
        }else{
            return Result.fail();
        }

    }

    /*根据医院id获取设置信息*/

    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable long id){
        try {
            //int i = 1/0;
        }catch (Exception e){
            throw new YyghException("失败",201);
        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }


    /*修改医院设置*/
    @PostMapping("updateHospSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);

        if (flag){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }


    /*批量删除医院设置*/
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospSet(@RequestBody List<Long> idList){
        boolean flag = hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    /*医院设置锁定解锁*/
    @PutMapping("lockHospSet/{id}/{status}")
    public Result lockHospSet(@PathVariable Long id,@PathVariable Integer status){
        //get data by id
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //set status
        hospitalSet.setStatus(status);
        //invoke method
        boolean flag = hospitalSetService.updateById(hospitalSet);

        return Result.ok();
    }

    //发送签名秘钥
    @PutMapping("sendKey/{id}")
    public Result sendKey(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //短信发送后面进行完善
        return Result.ok();

    }

}
