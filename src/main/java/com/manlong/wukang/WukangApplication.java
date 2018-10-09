package com.manlong.wukang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages= {"com.manlong.wukang.mapper"})
@SpringBootApplication
@EnableScheduling
public class WukangApplication {

    public static void main(String[] args) {
        SpringApplication.run(WukangApplication.class, args);
    }
}
