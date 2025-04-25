package edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.BillRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.BillResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BillMapper {
    Bill toEntity(BillRequestDto dto);
    BillResponseDto toResponse(Bill bill);
    List<BillResponseDto> toResponseList(List<Bill> bills);
}
