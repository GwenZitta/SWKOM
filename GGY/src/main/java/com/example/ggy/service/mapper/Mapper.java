package com.example.ggy.service.mapper;

public interface Mapper<S, T> {

    T mapToDto(S source);
}