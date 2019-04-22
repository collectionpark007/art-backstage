package com.art;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.art.mapper")
@SpringBootApplication
public class ArtBackstageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtBackstageApplication.class, args);
    }

}
