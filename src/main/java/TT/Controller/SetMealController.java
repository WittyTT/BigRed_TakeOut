package TT.Controller;

import TT.Common.R;
import TT.Service.CategoryService;
import TT.Service.SetMealDishService;
import TT.Service.SetMealService;
import TT.dto.DishDto;
import TT.dto.SetmealDto;
import TT.entity.Category;
import TT.entity.Dish;
import TT.entity.Setmeal;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private CategoryService categoryService;



    @GetMapping("/page")
    public R Page(int page, int pageSize, String name){
        log.info("page={},pagesize={},name={}",page,pageSize,name);
        Page<Setmeal> pageinfo = new Page(page, pageSize);
        Page<SetmealDto> setmealDtoPage=new Page<>();
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setMealService.page(pageinfo,lambdaQueryWrapper);
        //对象拷贝：
        BeanUtils.copyProperties(pageinfo,setmealDtoPage,"records");
        List<Setmeal> records = pageinfo.getRecords();
        List<SetmealDto>list=new ArrayList<>();
        for (Setmeal record : records) {
            SetmealDto setmealDto=new SetmealDto();
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            BeanUtils.copyProperties(record, setmealDto);
            list.add(setmealDto);
        }
        setmealDtoPage.setRecords(list);


        return R.success(setmealDtoPage);

    }
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
      setMealService.saveWithDish(setmealDto);
      return R.success("创建套餐成功！");
    }
    @DeleteMapping
    public R<String > delete(@RequestParam List<Long> ids){
        setMealService.deleteWithDish(ids);
        return R.success("删除成功！");
    }
    @PostMapping("/status/{s}")
    public R<String> EditStatus(@PathVariable int s,Long[] ids){

        for (Long id : ids) {
            Setmeal setmeal=setMealService.getById(id);
            log.info("状态值应设为：{}",s);
            setmeal.setStatus(s);
            setMealService.updateById(setmeal);
        }

        return R.success("状态修改成功！");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> GetSetMealList(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        List<Setmeal>list=setMealService.list(queryWrapper);
        return R.success(list);
    }
}
