package com.user.management.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataInfo {
    private String seed;
    private int results;
    private int page;
    private String version;
}
