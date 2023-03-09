package TT.Service.Impl;

import TT.Mapper.DishFlavorMapper;
import TT.Service.DishFlavorService;
import TT.entity.DishFlavor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>implements DishFlavorService {
}
