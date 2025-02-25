package org.example.medicalrecord.service;

import org.example.medicalrecord.data.dto.RecordDto;
import org.example.medicalrecord.data.entity.Record;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface RecordService {

    List<RecordDto> getRecords();

    RecordDto getRecord(long id);

    List<RecordDto> filterRecords(Specification<Record> specification);

    RecordDto createRecord(RecordDto recordDto);

    RecordDto updateRecord(RecordDto recordDto, long id);

    void deleteRecord(long id);
    
}
