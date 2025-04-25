package edu.miu.cs.cs489appsd.adsappointmentmanagement.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.common.RoleEnum;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.PatientRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.PatientResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.PatientSignupDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.exception.NotFoundException;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.mapper.PatientMapper;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Address;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.Patient;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.AddressRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.PatientRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final AddressRepository addressRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public PatientResponseDto signupPatient(PatientSignupDto dto) {
        Patient patient = new Patient();
        patient.setFirstName(dto.firstName());
        patient.setLastName(dto.lastName());
        patient.setPhoneNumber(dto.phoneNumber());
        patient.setEmail(dto.email());
        patient.setPassword(passwordEncoder.encode(dto.password()));
        patient.setRole(RoleEnum.PATIENT);
        patient.setDob(dto.dob());

        Address address = new Address();
        address.setStreet(dto.address().street());
        address.setCity(dto.address().city());
        address.setState(dto.address().state());
        address.setZip(dto.address().zip());
        address.setUnitNo(dto.address().unitNo());

        patient.setAddress(address);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.patientToPatientResponseDto(savedPatient);
    }

    @Override
    public List<PatientResponseDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<Patient> sortedPatients = patients.stream()
                .sorted(Comparator.comparing(Patient::getLastName)).toList();
        return patientMapper.patientListToPatientResponseDtoList(sortedPatients);
    }

    @Override
    public PatientResponseDto getPatientById(Long id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if(optionalPatient.isPresent()){
            return patientMapper.patientToPatientResponseDto(optionalPatient.get());
        }else{
            throw  new NotFoundException("Patient with id " +id + " not found" );
        }

    }

    @Override
    public PatientResponseDto updatePatient(Long id, PatientRequestDto patientRequestDto) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        Patient mappedPatient = patientMapper.patientRequestToPatient(patientRequestDto);
        if(optionalPatient.isPresent()){
            Patient foundPatient = optionalPatient.get();
            if(patientRequestDto.addressRequestDto() != null){
                Optional<Address> optionalAddress = addressRepository.findByUnitNo(patientRequestDto.addressRequestDto().unitNo());
                if(optionalAddress.isPresent()){
                    Address foundAddress = optionalAddress.get();
                    foundPatient.setAddress(foundAddress);
                }else{
                    foundPatient.setAddress(mappedPatient.getAddress());
                }
            }
            foundPatient.setDob(mappedPatient.getDob());
            foundPatient.setFirstName(mappedPatient.getFirstName());
            foundPatient.setLastName(mappedPatient.getLastName());
            foundPatient.setEmail(mappedPatient.getEmail());
            foundPatient.setPhoneNumber(mappedPatient.getPhoneNumber());
            Patient updatedPatient = patientRepository.save(foundPatient);
            return patientMapper.patientToPatientResponseDto(updatedPatient);

        }else{
            throw  new NotFoundException("Patient with id " +id + " not found" );
        }
    }

    @Override
    public void deletePatient(Long id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if(optionalPatient.isPresent()){
            patientRepository.delete(optionalPatient.get());
        }else{
            throw  new NotFoundException("Patient with id " +id + " not found" );
        }

    }

    @Override
    public List<PatientResponseDto> searchPatients(String searchString) {
        List<Patient> patients = patientRepository.searchPatients(searchString);
        return patientMapper.patientListToPatientResponseDtoList(patients);
    }

}
