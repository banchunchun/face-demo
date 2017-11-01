package com.arcvideo.face.feignclient.fallback;

import com.arcvideo.face.feignclient.LibraryClient;
import org.springframework.stereotype.Component;

/**
 * Created with idea
 * User: ben
 * Date:2017/11/1
 * Time:8:50
 */
@Component
public class LibraryClientFallBackHystrix implements LibraryClient {


    @Override
    public String addLibrary(String libName) {

        return "error";
    }
}
