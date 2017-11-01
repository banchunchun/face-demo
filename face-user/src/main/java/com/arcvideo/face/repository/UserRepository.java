package com.arcvideo.face.repository;

import com.arcvideo.face.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:19:49
 */
@Transactional
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {


}
