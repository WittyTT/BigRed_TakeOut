package TT.Controller;

import TT.Common.R;
import TT.Service.UserService;
import TT.entity.User;
import TT.utils.SMSUtils;
import TT.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        //获取手机号
        String phone = user.getPhone();
        if(phone!=null){
            //随机生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
//            SMSUtils.sendMessage("BigRed TakeOut","",phone,code);
            log.info("验证码为：{}",code);
            session.setAttribute(phone,code);
            return R.success("短信发送成功！");
        }


        //调用阿里云发短信

        //将验证码保存到session
        return R.error("短信发送失败！");

    }
    @PostMapping("/login")
    public R<User> Login(@RequestBody Map map, HttpSession session){
        //比对map验证码和session保存的验证码
        //判断是否为新用户，是的话自动注册
        String  phone = map.get("phone").toString();
        String code = map.get("code").toString();
        String codeInSession = session.getAttribute(phone).toString();
        if(codeInSession!=null&&code.equals(codeInSession)){
            LambdaQueryWrapper<User>queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user=userService.getOne(queryWrapper);
            if(user==null){
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
                return R.success(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败！");


    }

}
