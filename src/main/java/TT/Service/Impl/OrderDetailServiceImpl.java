package TT.Service.Impl;

import TT.Mapper.OrderDetailMapper;
import TT.Service.OrderDetailService;
import TT.entity.OrderDetail;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements OrderDetailService {
}
