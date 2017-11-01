package com.arcvideo.face.repository;

import com.arcvideo.face.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:20:17
 */
public interface LibraryRepository extends JpaRepository<Library, Integer>, JpaSpecificationExecutor<Library> {
}
