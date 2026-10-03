package com.hrgenius.attendance_service.controller;

import com.hrgenius.attendance_service.dto.AttendanceResponse;
import com.hrgenius.attendance_service.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/punch-in")
    public ResponseEntity<AttendanceResponse> punchIn(@RequestParam String employeeCode) {
        return ResponseEntity.ok(attendanceService.punchIn(employeeCode));
    }

    @PostMapping("/punch-out")
    public ResponseEntity<AttendanceResponse> punchOut(@RequestParam String employeeCode) {
        return ResponseEntity.ok(attendanceService.punchOut(employeeCode));
    }

    @GetMapping("/history/{employeeCode}")
    public ResponseEntity<List<AttendanceResponse>> getHistory(@PathVariable String employeeCode) {
        return ResponseEntity.ok(attendanceService.getEmployeeHistory(employeeCode));
    }
}