package TT.Service.Impl;

import TT.Mapper.ShoppingCartMapper;
import TT.Service.ShoppingCartService;
import TT.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
