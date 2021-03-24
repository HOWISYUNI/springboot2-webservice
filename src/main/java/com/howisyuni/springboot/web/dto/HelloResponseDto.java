package com.howisyuni.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// annotation으로 getter, 생성자 자동 생성
@Getter
@RequiredArgsConstructor
public class HelloResponseDto {
    
    private final String name;
    private final int amount;
}
