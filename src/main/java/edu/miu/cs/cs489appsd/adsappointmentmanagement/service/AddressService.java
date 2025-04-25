package edu.miu.cs.cs489appsd.adsappointmentmanagement.service;


import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AddressRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AddressResponseDto;

import java.util.List;

public interface AddressService {
    AddressResponseDto createAddress(AddressRequestDto addressRequestDto);
    List<AddressResponseDto> getAllAddresses();
    AddressResponseDto getAddressById(long id);
    AddressResponseDto updateAddress(long id, AddressRequestDto addressRequestDto);
}
