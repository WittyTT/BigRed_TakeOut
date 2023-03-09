package TT.Service.Impl;

import TT.Common.CustomException;
import TT.Mapper.DishMapper;
import TT.Service.DishFlavorService;
import TT.Service.DishService;
import TT.dto.DishDto;
import TT.entity.Dish;
import TT.entity.DishFlavor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private  DishService dishService;
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品信息到dish
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }

        dishFlavorService.saveBatch(flavors);
        //保存口味数据到口味表

    }

    @Override
    public DishDto getByIdWithFlavour(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;


    }
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        //先清理口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        Long id = dishDto.getId();


        //再添加口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);

    }
    @Transactional
    @Override
    public void deleteWithFlavour(Long[] ids) {
        for (Long id : ids) {

            LambdaQueryWrapper<Dish>queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.in(Dish::getId,id);
            queryWrapper.eq(Dish::getStatus,1);
            int count=this.count(queryWrapper);
            if(count>0){
                throw new CustomException("请先停售菜品再删除！");
            }
            else {
                this.removeById(id);
                LambdaQueryWrapper<DishFlavor>queryWrapper1=new LambdaQueryWrapper<>();
                queryWrapper1.eq(DishFlavor::getDishId,id);
                dishFlavorService.remove(queryWrapper1);

            }

        }

    }


}
