package edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.SurgeryRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.SurgeryResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Surgery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AddressMapper.class)
public interface SurgeryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "location", target = "location")
    Surgery toSurgery(SurgeryRequestDto dto);

    @Mapping(source = "location", target = "location")
    SurgeryResponseDto toSurgeryResponseDto(Surgery surgery);

    List<SurgeryResponseDto> toSurgeryResponseDtoList(List<Surgery> surgeries);
}
