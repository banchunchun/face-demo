package com.arcvideo.face;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/30
 * Time:10:08
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class StartClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartClientApplication.class,args);
    }
}
