package org.example.medicalrecord.util;

import org.example.medicalrecord.data.dto.SpecialityDto;
import org.example.medicalrecord.data.entity.Role;
import org.example.medicalrecord.data.entity.Speciality;
import org.modelmapper.Converter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class MapConverters {

    private MapConverters() {
    }

    public static final Converter<Speciality, SpecialityDto> SPECIALITY_TO_STRING =
            context -> {
                Speciality source = context.getSource();
                return (source == null) ? null : new SpecialityDto(source.getId(), source.getSpecialtyName());
            };

    public static final Converter<Role, String> ROLE_TO_STRING =
            context -> {
                Role source = context.getSource();
                return (source == null) ? null : source.getAuthority();
            };

    public static final Converter<Date, LocalDate> DATE_TO_LOCAL_DATE = context -> {
        Date source = context.getSource();
        if (source == null) {
            return null;
        }
        return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    };

    public static final Converter<LocalDate, Date> LOCAL_DATE_TO_DATE =
            context -> context.getSource() == null ? null
                    : Date.from(context.getSource().atStartOfDay(ZoneId.systemDefault()).toInstant());

}
