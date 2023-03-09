package TT.Service.Impl;

import TT.Mapper.EmployeeMapper;
import TT.Service.EmployeeService;
import TT.entity.Employee;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
@org.springframework.stereotype.Service
public class Service extends ServiceImpl<EmployeeMapper, Employee>implements EmployeeService {
}
