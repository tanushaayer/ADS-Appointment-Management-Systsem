package edu.miu.cs.cs489appsd.adsappointmentmanagement.controller;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.DentistRequestDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.DentistResponseDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.signup.DentistSignUpDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.service.DentistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dentists")
@RequiredArgsConstructor
public class DentistController {

    private final DentistService dentistService;

    @GetMapping
    public ResponseEntity<List<DentistResponseDto>> getAll() {
        return ResponseEntity.ok(dentistService.getAllDentists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dentistService.getDentistById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DentistResponseDto> update(@PathVariable Long id, @Valid @RequestBody DentistRequestDto dto) {
        return ResponseEntity.ok(dentistService.updateDentist(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dentistService.deleteDentist(id);
        return ResponseEntity.noContent().build();
    }
}
