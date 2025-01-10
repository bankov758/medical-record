package org.example.medicalrecord.util;

import org.example.medicalrecord.data.dto.PatientDto;
import org.example.medicalrecord.data.dto.SpecialityDto;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.data.entity.Speciality;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration(enforceUniqueMethods = false)
public class ModelMapperUtil {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        //Speciality to SpecialityDto
        Converter<Speciality, SpecialityDto> someObjectToStringConverter =
                context -> {
                    Speciality source = context.getSource();
                    return (source == null) ? null : new SpecialityDto(source.getId(), source.getSpecialtyName());
                };
        modelMapper.createTypeMap(Speciality.class, SpecialityDto.class).setConverter(someObjectToStringConverter);

        //PatientDto to Patient
        Converter<LocalDate, Date> localDateToDateConverter =
                context -> context.getSource() == null ? null
                        : Date.from(context.getSource().atStartOfDay(ZoneId.systemDefault()).toInstant());
        modelMapper.typeMap(PatientDto.class, Patient.class)
                .addMappings(mapper -> {
                    mapper.skip(Patient::setId);
                    mapper.using(localDateToDateConverter).map(PatientDto::getLastPaidMedicalInsurance, Patient::setLastPaidMedicalInsurance);
                });

        //Patient to PatientDto
        Converter<Date, LocalDate> dateToLocalDateConverter = ctx -> {
            Date source = ctx.getSource();
            if (source == null) {
                return null;
            }
            return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        };
        modelMapper.typeMap(Patient.class, PatientDto.class)
                .addMappings(mapper ->
                        mapper.using(dateToLocalDateConverter).map(Patient::getLastPaidMedicalInsurance, PatientDto::setLastPaidMedicalInsurance)
                );

        return modelMapper;
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

