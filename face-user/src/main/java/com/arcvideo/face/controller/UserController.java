package com.arcvideo.face.controller;

import com.arcvideo.face.feignclient.LibraryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:13:37
 */
@RestController
public class UserController {

    @Autowired
    private LibraryClient libraryClient;
    @RequestMapping("/test")
    public String test(String libName) {

        return libraryClient.addLibrary(libName);
    }
}
