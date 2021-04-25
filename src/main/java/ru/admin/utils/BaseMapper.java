package ru.admin.utils;

import org.springframework.beans.BeanUtils;

public class BaseMapper {
    public static <S, T> T map(S source, Class<T> targetClass) {
        T result = BeanUtils.instantiateClass(targetClass);
        BeanUtils.copyProperties(source, result);
        return result;
    }
}
