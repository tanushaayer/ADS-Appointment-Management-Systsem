package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AddressRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AddressResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.NotFoundException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.AddressMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Address;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.AddressRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto) {
       Address savedAddress =  addressRepository.save(addressMapper.addressRequestDtoToAddress(addressRequestDto));
       return addressMapper.addressToAddressResponseDto(savedAddress);
    }

    @Override
    public List<AddressResponseDto> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return  addressMapper.addressListToAddressResponseDtoList(addresses);
    }

    @Override
    public AddressResponseDto getAddressById(long id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if(optionalAddress.isPresent()) {
            return addressMapper.addressToAddressResponseDto(optionalAddress.get());
        }else {
            throw new NotFoundException("Address with id " +id + " doesn't exist");
        }
    }

    @Override
    public AddressResponseDto updateAddress(long id, AddressRequestDto addressRequestDto) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if(optionalAddress.isPresent()) {
            Address foundAddress = optionalAddress.get();
            foundAddress.setUnitNo(addressRequestDto.unitNo());
            foundAddress.setCity(addressRequestDto.city());
            foundAddress.setCity(addressRequestDto.city());
            foundAddress.setState(addressRequestDto.state());
            foundAddress.setZip(addressRequestDto.zip());
            Address updatedAddress = addressRepository.save(foundAddress);
            return addressMapper.addressToAddressResponseDto(updatedAddress);
        }else {
            throw new NotFoundException("Address with id " +id + " doesn't exist");
        }
    }

}
