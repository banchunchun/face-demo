package com.arcvideo.face.feignclient;

import com.arcvideo.face.feignclient.fallback.LibraryClientFallBackHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:20:24
 */
@FeignClient(name = "library-service",fallback = LibraryClientFallBackHystrix.class)
public interface LibraryClient {

    @RequestMapping("/lib/add")
    String addLibrary(@RequestParam(name = "libName") String libName);
}
