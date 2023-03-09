package TT.Controller;

import TT.Common.BaseContext;
import TT.Common.R;
import TT.Service.ShoppingCartService;
import TT.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> Add(@RequestBody ShoppingCart shoppingCart){
        Long user = BaseContext.getCurrentId();
        shoppingCart.setUserId(user);
        LambdaQueryWrapper<ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,user);
        if(shoppingCart.getDishId()==null){
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        else{
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
        if(shoppingCartService.count(queryWrapper)>0){
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);
            shoppingCartService.updateById(shoppingCart1);
        }
        else{
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCart1=shoppingCart;
        }
        return R.success(shoppingCart1);
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> List(){
        Long id = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,id);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);

    }
    @PostMapping("/sub")
    public R<String> Sub(@RequestBody ShoppingCart shoppingCart){
        Long user = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,user);
        if(shoppingCart.getDishId()==null){
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
            if(shoppingCart1.getNumber()==0){
                return R.error("该菜品数量已为零！");
            }
            shoppingCart1.setNumber(shoppingCart1.getNumber()-1);
            shoppingCartService.updateById(shoppingCart1);
        }
        else{
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
            if(shoppingCart1.getNumber()==0) {
                return R.error("该菜品数量已为零！");
            }
            shoppingCart1.setNumber(shoppingCart1.getNumber()-1);
            shoppingCartService.updateById(shoppingCart1);

        }
        return R.success("删除成功！");

    }
    @DeleteMapping("/clean")
    public R<String> Clean(){
        Long id = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,id);
        shoppingCartService.remove(queryWrapper);
       return R.success("购物车清空成功！");
    }
}
