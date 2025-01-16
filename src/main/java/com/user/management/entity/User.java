package com.user.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Entity(name = "\"user\"")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Username cannot be null")
    private String userName;
    private String name;
    @NotNull(message = "Email cannot be null")
    private String email;
    private String gender;
    private String picture;
}
