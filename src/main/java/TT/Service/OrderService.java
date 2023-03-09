package TT.Service;

import TT.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
