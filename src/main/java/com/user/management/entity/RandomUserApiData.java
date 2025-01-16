package com.user.management.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class RandomUserApiData {

    private List<RandomUser> results;
    private DataInfo info;
}
