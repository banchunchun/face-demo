package com.arcvideo.face.controller;

import com.arcvideo.face.model.Library;
import com.arcvideo.face.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:17:33
 */
@RestController
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @RequestMapping("/lib/add")
    public String add(@RequestParam String libName) {
        Library library = new Library();
        library.setLibName(libName);
        libraryService.addLibrary(library);
        return "ok";
    }
}
