package org.example.medicalrecord.util;

import org.example.medicalrecord.data.dto.PatientDto;
import org.example.medicalrecord.data.dto.RecordDto;
import org.example.medicalrecord.data.dto.SpecialityDto;
import org.example.medicalrecord.data.entity.Patient;
import org.example.medicalrecord.data.entity.Record;
import org.example.medicalrecord.data.entity.Role;
import org.example.medicalrecord.data.entity.Speciality;
import org.example.medicalrecord.web.view.model.RecordViewModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration(enforceUniqueMethods = false)
public class ModelMapperUtil {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        addSpecialityMappings(modelMapper);
        addRoleMappings(modelMapper);
        addDatePatientMappings(modelMapper);
        addRecordMappings(modelMapper);

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

    private static void addRecordMappings(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<Record, RecordDto>() {
            @Override
            protected void configure() {
                map(source.getDoctor().getId(), destination.getDoctorId());
                map(source.getDoctor().getFirstName(), destination.getDoctorFirstName());
                map(source.getDoctor().getLastName(), destination.getDoctorLastName());
                map(source.getPatient().getFirstName(), destination.getPatientFirstName());
                map(source.getPatient().getLastName(), destination.getPatientLastName());
                map(source.getPatient().getEgn(), destination.getPatientEgn());
                map(source.getDiagnose().getDiagnoseName(), destination.getDiagnoseName());
                map(source.getDiagnose().getReceipt(), destination.getReceipt());
                map(source.getSickLeave().getStartDate(), destination.getStartDate());
                map(source.getSickLeave().getLeaveDays(), destination.getLeaveDays());
            }
        });
        modelMapper.typeMap(RecordDto.class, RecordViewModel.class)
                .addMappings(mapper -> {
                    mapper.using(MapConverters.DATE_TO_LOCAL_DATE).map(RecordDto::getVisitDate, RecordViewModel::setVisitDate);
                    mapper.using(MapConverters.DATE_TO_LOCAL_DATE).map(RecordDto::getStartDate, RecordViewModel::setStartDate);
                });
        modelMapper.typeMap(RecordViewModel.class, RecordDto.class)
                .addMappings(mapper -> {
                    mapper.using(MapConverters.LOCAL_DATE_TO_DATE).map(RecordViewModel::getVisitDate, RecordDto::setVisitDate);
                    mapper.using(MapConverters.LOCAL_DATE_TO_DATE).map(RecordViewModel::getStartDate, RecordDto::setStartDate);
                });
    }

    private static void addSpecialityMappings(ModelMapper modelMapper) {
        modelMapper.createTypeMap(Speciality.class, SpecialityDto.class).setConverter(MapConverters.SPECIALITY_TO_STRING);
    }

    private static void addRoleMappings(ModelMapper modelMapper) {
        modelMapper.createTypeMap(Role.class, String.class).setConverter(MapConverters.ROLE_TO_STRING);
    }

    private static void addDatePatientMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(Patient.class, PatientDto.class)
                .addMappings(mapper ->
                        mapper.using(MapConverters.DATE_TO_LOCAL_DATE).map(Patient::getLastPaidMedicalInsurance, PatientDto::setLastPaidMedicalInsurance)
                );
        modelMapper.typeMap(PatientDto.class, Patient.class)
                .addMappings(mapper -> {
                    mapper.skip(Patient::setId);
                    mapper.using(MapConverters.LOCAL_DATE_TO_DATE).map(PatientDto::getLastPaidMedicalInsurance, Patient::setLastPaidMedicalInsurance);
                });
    }

}

