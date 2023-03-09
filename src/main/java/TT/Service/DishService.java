package TT.Service;

import TT.dto.DishDto;
import TT.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品口味数据
    public void saveWithFlavor(DishDto dishDto);
    public  DishDto getByIdWithFlavour(Long id);

    void updateWithFlavor(DishDto dishDto);
    void deleteWithFlavour(Long[] id);
}

