package com.hrgenius.attendance_service.dto;

import com.hrgenius.attendance_service.entity.AttendanceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class AttendanceResponse {
    private Long id;
    private String employeeCode;
    private LocalDate attendanceDate;
    private LocalDateTime punchInTime;
    private LocalDateTime punchOutTime;
    private Double totalHours;
    private AttendanceStatus status;
}