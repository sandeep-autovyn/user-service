package com.user.management.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Login {
    private String uuid;
    private String username;
    private String password;
    private String salt;
    private String md5;
    private String sha1;
    private String sha256;
}
