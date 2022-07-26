package com.youngjo.ssg.global.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SecurityLoginResDto {
    private Long id;
    private String name;
    private String email;
}
