package com.hrgenius.attendance_service.repository;

import com.hrgenius.attendance_service.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEmployeeCodeAndAttendanceDate(String employeeCode, LocalDate attendanceDate);
    List<Attendance> findByEmployeeCodeOrderByAttendanceDateDesc(String employeeCode);
    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);
}