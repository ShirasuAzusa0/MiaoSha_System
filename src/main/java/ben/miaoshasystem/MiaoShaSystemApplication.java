package ben.miaoshasystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 启动类中加上 EnableScheduling 注解才能启用定时任务
@EnableScheduling
public class MiaoShaSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoShaSystemApplication.class, args);
    }

}
