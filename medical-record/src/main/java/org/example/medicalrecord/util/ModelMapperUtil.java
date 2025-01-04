package org.example.medicalrecord.util;

import org.example.medicalrecord.data.entity.Speciality;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Configuration(enforceUniqueMethods = false)
public class ModelMapperUtil {

    @Bean
    public ModelMapper getModelMapper() {
        Converter<Speciality, String> someObjectToStringConverter =
                context -> {
                    Speciality source = context.getSource();
                    return (source == null) ? null : source.getSpecialtyName();
                };

        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(Speciality.class, String.class)
                .setConverter(someObjectToStringConverter);
        return mapper;
    }

    @Bean
    public <S, T> ModelMapper getModelMapper(Class<S> source, Class<T> targetClass, Converter<S, T> converter) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(source, targetClass)
                .setConverter(converter);
        return modelMapper;
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> getModelMapper().map(element, targetClass))
                .collect(Collectors.toList());
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass, ModelMapper mapper) {
        return source
                .stream()
                .map(element -> mapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

}

