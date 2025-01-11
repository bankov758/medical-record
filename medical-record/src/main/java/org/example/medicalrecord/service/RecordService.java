package org.example.medicalrecord.service;

import org.example.medicalrecord.data.dto.RecordDto;

import java.util.List;

public interface RecordService {

    List<RecordDto> getRecords();

    RecordDto getRecord(long id);

    RecordDto createRecord(RecordDto recordDto);

    RecordDto updateRecord(RecordDto recordDto, long id);

    void deleteRecord(long id);
    
}
