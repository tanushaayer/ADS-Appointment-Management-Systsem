package edu.miu.cs.cs489appsd.adsappointmentmanagement.service;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestApprovalDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.request.AppointmentRequestSubmissionDto;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.dto.response.AppointmentRequestResponseDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AppointmentRequestService {
    AppointmentRequestResponseDto submitRequest(AppointmentRequestSubmissionDto dto, Authentication authentication);
    List<AppointmentRequestResponseDto> getAllRequests();
    AppointmentRequestResponseDto approveRequest(Long requestId, AppointmentRequestApprovalDto approvalDto);
    void rejectRequest(Long requestId);
}
