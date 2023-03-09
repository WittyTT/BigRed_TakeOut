package TT.Controller;

import TT.Common.R;
import TT.Service.CategoryService;
import TT.Service.DishFlavorService;
import TT.Service.DishService;
import TT.dto.DishDto;
import TT.entity.Category;
import TT.entity.Dish;
import TT.entity.DishFlavor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @GetMapping("/page")
    public R Page(int page, int pageSize, String name){
        log.info("page={},pagesize={},name={}",page,pageSize,name);
        Page<Dish> pageinfo = new Page(page, pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageinfo,lambdaQueryWrapper);
        //对象拷贝：
        BeanUtils.copyProperties(pageinfo,dishDtoPage,"records");
        List<Dish> records = pageinfo.getRecords();
        List<DishDto>list=new ArrayList<>();
        for (Dish record : records) {
            DishDto dishDto=new DishDto();
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
                BeanUtils.copyProperties(record, dishDto);
                list.add(dishDto);
        }
        dishDtoPage.setRecords(list);


        return R.success(dishDtoPage);

    }
    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
       log.info("数据为：{}",dishDto);
       dishService.saveWithFlavor(dishDto);

       return R.success("菜品新建成功！");
    }
    @GetMapping("/{id}")
    public  R<DishDto> updateDisplay(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavour(id);
        return R.success(dishDto);

    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("数据为：{}",dishDto);
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品更新成功！");
    }
    @DeleteMapping
    public R<String> Delete(Long []ids){
      dishService.deleteWithFlavour(ids);
      return R.success("删除成功！");

    }
    @PostMapping("/status/{s}")
    public R<String> EditStatus(@PathVariable int s,Long[] ids){

            for (Long id : ids) {
                Dish dish=dishService.getById(id);
                log.info("状态值应设为：{}",s);
                dish.setStatus(s);
                dishService.updateById(dish);
            }

        return R.success("状态修改成功！");
    }
//    @GetMapping("/list")
//    public R<List<Dish>> UpdateInSetMeal(Dish dish){
//        LambdaQueryWrapper<Dish>queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish>list=dishService.list(queryWrapper);
//      return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> UpdateInSetMeal(Dish dish){
        LambdaQueryWrapper<Dish>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish>list=dishService.list(queryWrapper);
        List<DishDto>listDto=new ArrayList<>();
        for (Dish record : list) {
            DishDto dishDto=new DishDto();
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long id = record.getId();
            LambdaQueryWrapper<DishFlavor>queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,id);
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavors);
            BeanUtils.copyProperties(record, dishDto);
            listDto.add(dishDto);
        }


        return R.success(listDto);
    }
}
