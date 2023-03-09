package TT.Service;

import TT.dto.SetmealDto;
import TT.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    void deleteWithDish(List<Long> ids);
}
