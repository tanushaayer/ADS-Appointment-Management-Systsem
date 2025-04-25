package edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AddressRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AddressResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    //dto to address
    Address addressRequestDtoToAddress(AddressRequestDto addressRequestDto);

    //address to response
    AddressResponseDto addressToAddressResponseDto(Address address);

    //List of address to List of response
    List<AddressResponseDto> addressListToAddressResponseDtoList(List<Address> addresses);
}
