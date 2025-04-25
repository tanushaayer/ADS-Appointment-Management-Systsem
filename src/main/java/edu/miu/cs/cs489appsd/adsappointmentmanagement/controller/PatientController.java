package edu.miu.cs.cs489appsd.adsappointmentmanagement.controller;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.PatientRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.PatientResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adsweb/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        List<PatientResponseDto> patients = patientService.getAllPatients();
        return ResponseEntity.status(HttpStatus.CREATED).body(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable long id) {
        PatientResponseDto patient = patientService.getPatientById(id);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable long id, @RequestBody PatientRequestDto patientRequestDto) {
        PatientResponseDto patient = patientService.updatePatient(id, patientRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PatientResponseDto> deletePatient(@PathVariable long id) {
        patientService.deletePatient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<PatientResponseDto>> searchPatient(@PathVariable String searchString) {
        List<PatientResponseDto> result = patientService.searchPatients(searchString);
        return ResponseEntity.ok(result);
    }

}
