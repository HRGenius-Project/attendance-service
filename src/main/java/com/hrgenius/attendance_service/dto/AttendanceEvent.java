package com.hrgenius.attendance_service.dto;

import com.hrgenius.attendance_service.entity.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceEvent implements Serializable {
    private String eventType;      // PUNCH_IN ya PUNCH_OUT
    private String employeeCode;
    private LocalDate attendanceDate;
    private LocalDateTime timestamp;
    private Double totalHours;
    private AttendanceStatus status;
}