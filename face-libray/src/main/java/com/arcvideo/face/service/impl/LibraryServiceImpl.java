package com.arcvideo.face.service.impl;

import com.arcvideo.face.model.Library;
import com.arcvideo.face.repository.LibraryRepository;
import com.arcvideo.face.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:20:20
 */
@Service
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public Library addLibrary(Library library) {

        return libraryRepository.save(library);
    }
}
