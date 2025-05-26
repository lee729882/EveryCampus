package com.everycampus.controller;

import com.everycampus.dto.TimetableRequest;
import com.everycampus.entity.Timetable;
import com.everycampus.service.TimetableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveTimetable(@RequestBody TimetableRequest request) {
        String studentId = request.getStudentId();
        // 시간표 데이터를 받아서 각 수업을 저장
        for (TimetableRequest.TimetableRow row : request.getTimetable()) {
            // 각 요일별 수업을 저장하는 로직
            saveTimetableRow(studentId, row);
        }
        return ResponseEntity.ok("시간표가 저장되었습니다.");
    }

    private void saveTimetableRow(String studentId, TimetableRequest.TimetableRow row) {
        if (row.getMon() != null) {
            timetableService.save(new Timetable(studentId, "Mon", row.getTime(), row.getMon()));
        }
        if (row.getTue() != null) {
            timetableService.save(new Timetable(studentId, "Tue", row.getTime(), row.getTue()));
        }
        if (row.getWed() != null) {
            timetableService.save(new Timetable(studentId, "Wed", row.getTime(), row.getWed()));
        }
        if (row.getThu() != null) {
            timetableService.save(new Timetable(studentId, "Thu", row.getTime(), row.getThu()));
        }
        if (row.getFri() != null) {
            timetableService.save(new Timetable(studentId, "Fri", row.getTime(), row.getFri()));
        }
    }


    @GetMapping("/load/{studentId}")
    @ResponseBody
    public ResponseEntity<List<Timetable>> loadTimetable(@PathVariable String studentId) {
        List<Timetable> timetable = timetableService.getTimetableByStudentId(studentId);
        if (timetable.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(timetable);
    }

    
    
}