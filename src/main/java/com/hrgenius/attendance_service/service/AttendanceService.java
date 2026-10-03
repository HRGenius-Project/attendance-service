package com.hrgenius.attendance_service.service;

import com.hrgenius.attendance_service.dto.AttendanceResponse;
import com.hrgenius.attendance_service.entity.Attendance;
import com.hrgenius.attendance_service.entity.AttendanceStatus;
import com.hrgenius.attendance_service.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceResponse punchIn(String employeeCode) {
        LocalDate today = LocalDate.now();

        attendanceRepository.findByEmployeeCodeAndAttendanceDate(employeeCode, today)
                .ifPresent(a -> {
                    throw new RuntimeException(
                            "Already punched in for today: " + today
                    );
                });

        Attendance attendance = Attendance.builder()
                .employeeCode(employeeCode)
                .attendanceDate(today)
                .punchInTime(LocalDateTime.now())
                .status(AttendanceStatus.PRESENT)
                .build();

        return mapToResponse(attendanceRepository.save(attendance));
    }

    public AttendanceResponse punchOut(String employeeCode) {
        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeCodeAndAttendanceDate(employeeCode, today)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No punch-in record found for today"
                        )
                );

        if (attendance.getPunchOutTime() != null) {
            throw new RuntimeException("Already punched out for today");
        }

        LocalDateTime punchOut = LocalDateTime.now();
        attendance.setPunchOutTime(punchOut);

        // Hours calculate karo
        long minutes = Duration.between(
                attendance.getPunchInTime(),
                punchOut
        ).toMinutes();

        double hours = Math.round(
                (minutes / 60.0) * 100.0
        ) / 100.0;

        attendance.setTotalHours(hours);

        if (hours < 4.0) {
            attendance.setStatus(AttendanceStatus.HALF_DAY);
        } else {
            attendance.setStatus(AttendanceStatus.PRESENT);
        }

        return mapToResponse(
                attendanceRepository.save(attendance)
        );
    }

    public List<AttendanceResponse> getEmployeeHistory(
            String employeeCode
    ) {
        return attendanceRepository
                .findByEmployeeCodeOrderByAttendanceDateDesc(employeeCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AttendanceResponse mapToResponse(
            Attendance attendance
    ) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .employeeCode(attendance.getEmployeeCode())
                .attendanceDate(attendance.getAttendanceDate())
                .punchInTime(attendance.getPunchInTime())
                .punchOutTime(attendance.getPunchOutTime())
                .totalHours(attendance.getTotalHours())
                .status(attendance.getStatus())
                .build();
    }
}