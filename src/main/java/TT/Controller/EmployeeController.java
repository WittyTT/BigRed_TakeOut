package TT.Controller;

import TT.Common.R;
import TT.Service.EmployeeService;
import TT.entity.Employee;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
//import org.springframework.util.StringUtils;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.http.HttpServletRequest;
import java.security.DigestException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(QueryWrapper);
        if(emp==null)
            return R.error("登录失败");
        if(!emp.getPassword().equals(password))
            return R.error("登录失败");
        if(emp.getStatus()==0)
            return R.error("账号已禁用");
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){

        log.info("新增员工，员工信息{}",employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        long empId = (long)request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功！");
    }
@GetMapping("/page")
    public R<Page> Page(int page,int pageSize,String name){
        log.info("page={},pagesize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper =new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);


    }
    //修改员工信息
    @PutMapping
    public R<String>Update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Long empID=(Long)request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empID);
        employeeService.updateById(employee);
        return  R.success("员工信息修改成功");
    }
    @GetMapping("/{id}")
    public R<Employee>GetByID(@PathVariable Long id){
        log.info("根据ID查询员工");
        Employee employee = employeeService.getById(id);
        if(employee!=null)
        return R.success(employee);
        else
            return R.error("查询错误");
    }

}
