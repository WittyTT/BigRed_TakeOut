package TT.Service.Impl;

import TT.Mapper.UserMapper;
import TT.Service.UserService;
import TT.entity.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
