package com.arcvideo.face.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with idea
 * User: ben
 * Date:2017/10/31
 * Time:17:33
 */
@Table
@Entity
public class Library implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int libId;


    private String libName;


    public int getLibId() {
        return libId;
    }

    public void setLibId(int libId) {
        this.libId = libId;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }
}
