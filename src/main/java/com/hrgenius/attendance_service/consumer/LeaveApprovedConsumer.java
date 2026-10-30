package com.hrgenius.attendance_service.consumer;

import com.hrgenius.attendance_service.dto.LeaveApprovedEvent;
import com.hrgenius.attendance_service.entity.Attendance;
import com.hrgenius.attendance_service.entity.AttendanceStatus;
import com.hrgenius.attendance_service.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeaveApprovedConsumer {

    private final AttendanceRepository attendanceRepository;

    @RabbitListener(queues = "${app.rabbitmq.leave-queue}")
    @Transactional
    public void handleLeaveApproved(LeaveApprovedEvent event) {
        log.info("Processing leave approval for employee: {} from {} to {}",
                event.getEmployeeCode(), event.getStartDate(), event.getEndDate());

        LocalDate current = event.getStartDate();
        LocalDate end = event.getEndDate();

        while (!current.isAfter(end)) {
            final LocalDate dateToRecord = current;
            boolean alreadyExists = attendanceRepository
                    .findByEmployeeCodeAndAttendanceDate(event.getEmployeeCode(), dateToRecord)
                    .isPresent();

            if (!alreadyExists) {
                Attendance leaveAttendance = Attendance.builder()
                        .employeeCode(event.getEmployeeCode())
                        .attendanceDate(dateToRecord)
                        .status(AttendanceStatus.ON_LEAVE)
                        .totalHours(0.0)
                        .build();

                attendanceRepository.save(leaveAttendance);
                log.info("Marked date {} as ON_LEAVE for employee {}", dateToRecord, event.getEmployeeCode());
            }

            current = current.plusDays(1);
        }
    }
}