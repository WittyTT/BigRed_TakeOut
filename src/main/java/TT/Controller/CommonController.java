package TT.Controller;

import TT.Common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${BigRed.path}")
    private String BasePath;
    @PostMapping("/upload")
    public R<String> Upload(MultipartFile file){
        //file是临时文件，需要转存到指定目录
       log.info(file.toString());
       //原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取原始文件名扩展名
        String originalFileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止重名覆盖
        String newFilename = UUID.randomUUID().toString()+originalFileSuffix;
        //创建一个目录对象
        File dir=new File(BasePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(BasePath+newFilename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(newFilename);
    }
    //文件下载
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(BasePath+name));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[]bytes=new byte[1024];
            response.setContentType("image/jpeg");
            int length=0;
            while((length=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,length);
                outputStream.flush();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //输出流，通过输出流将文件写回浏览器
    }
}
