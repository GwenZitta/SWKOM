package com.example.ggy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class documentDto implements Serializable {
    private Long id;
    private String name;
    private String documenttype;
    private String pathtodocument;
    private String datetime;
}
