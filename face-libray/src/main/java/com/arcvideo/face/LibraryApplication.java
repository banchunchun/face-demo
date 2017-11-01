package com.arcvideo.face;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:17:32
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LibraryApplication {


    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class,args);
    }
}
