package com.jngyen.bookkeeping.backend.factory.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CommonDtoFactory {
    public static <T, D> D convertToDto(T po, Class<D> dtoClass) {
        try{
            D dto = dtoClass.getConstructor().newInstance();
            BeanUtils.copyProperties(po, dto);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    public static <T, D> List<D> convertToDto(List<T> pos, Class<D> dtoClass) {
        return pos.stream()
                .map(po->convertToDto(po, dtoClass))
                .collect(Collectors.toList());
    }
}
