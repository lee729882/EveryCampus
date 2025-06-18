package com.everycampus.controller;

import com.everycampus.dto.TimetableRequest;
import com.everycampus.entity.Timetable;
import com.everycampus.entity.User;
import com.everycampus.service.TimetableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import com.everycampus.repository.UserRepository;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    private final TimetableService timetableService;
    private final UserRepository userRepository;

    public TimetableController(TimetableService timetableService, UserRepository userRepository) {
        this.timetableService = timetableService;
        this.userRepository = userRepository;
    }


    @PostMapping("/save")
    public ResponseEntity<String> saveTimetable(@RequestBody TimetableRequest request,
                                                Principal principal) {
        String username = (principal != null) ? principal.getName() : "guest";
        User user = userRepository.findByUsername(username).orElse(null);

        for (TimetableRequest.TimetableRow row : request.getTimetable()) {
            saveTimetableRow(request.getStudentId(), row, user);
        }

        return ResponseEntity.ok("저장 완료");
    }



    private void saveTimetableRow(String studentId, TimetableRequest.TimetableRow row, User user) {
        if (row.getMon() != null) {
            timetableService.save(new Timetable(studentId, "Mon", row.getTime(), row.getMon()), user);
        }
        if (row.getTue() != null) {
            timetableService.save(new Timetable(studentId, "Tue", row.getTime(), row.getTue()), user);
        }
        if (row.getWed() != null) {
            timetableService.save(new Timetable(studentId, "Wed", row.getTime(), row.getWed()), user);
        }
        if (row.getThu() != null) {
            timetableService.save(new Timetable(studentId, "Thu", row.getTime(), row.getThu()), user);
        }
        if (row.getFri() != null) {
            timetableService.save(new Timetable(studentId, "Fri", row.getTime(), row.getFri()), user);
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