package TT.Controller;

import TT.Common.R;
import TT.Service.CategoryService;
import TT.Service.Impl.Service;
import TT.entity.Category;
import TT.entity.Employee;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeMessageFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String>Save(@RequestBody Category category){
        log.info("Category:{}" ,category);
        categoryService.save(category);
        return R.success("新增成功");

    }
    @GetMapping("/page")
    public R<Page> Page(int page, int pageSize){
        log.info("page={},pagesize={}",page,pageSize);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper =new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);

        //执行查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);


    }
    //根据ID删除分类
    @DeleteMapping
    public R<String> Delete(Long ids){
        log.info("id is{}",ids);

//        categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("删除成功！");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息{}",category);
        categoryService.updateById(category);
        return R.success("修改成功！");
    }
    //根据条件查询分类数据
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);

    }
}
