package TT.Service.Impl;

import TT.Common.CustomException;
import TT.Mapper.CategoryMapper;
import TT.Service.CategoryService;
import TT.Service.DishService;
import TT.Service.SetMealService;
import TT.entity.Category;
import TT.entity.Dish;
import TT.entity.Setmeal;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceIml extends ServiceImpl<CategoryMapper, Category>implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;
    @Override
    public void remove(Long id) {
        //查询是否关联菜品，分类是否关联套餐
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count>0){
            //有关联菜品，抛出异常
            throw new CustomException("当前分类关联菜品，无法删除！");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setMealService.count(setmealLambdaQueryWrapper);
        if(count1>0){
            //有关联套餐 ，抛出异常
            throw new CustomException("当前分类关联套餐，无法删除！");
        }
        super.removeById(id);


    }
}
