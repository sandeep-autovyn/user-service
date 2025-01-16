package com.user.management.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Location {
    private Object street;
    private String city;
    private String state;
    private String country;
    private String postcode;
    private Object coordinates;
    private Object timezone;
}
