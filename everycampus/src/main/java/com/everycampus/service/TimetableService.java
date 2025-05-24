package com.everycampus.service;

import com.everycampus.dto.TimetableRequest;
import com.everycampus.entity.Timetable;
import com.everycampus.entity.User; // User 엔티티 추가
import com.everycampus.repository.UserRepository; // UserRepository 추가
import com.everycampus.repository.TimetableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository; // UserRepository 추가

    public TimetableService(TimetableRepository timetableRepository, UserRepository userRepository) {
        this.timetableRepository = timetableRepository;
        this.userRepository = userRepository; // 생성자에서 UserRepository 주입
    }
    
    public String getSubjectOrDefault(String subject) {
        return (subject == null) ? "" : subject;
    }
    // 시간표 저장
    public void save(Timetable timetable) {
        timetableRepository.save(timetable);
    }

    // TimetableRequest를 받아 시간표 저장
    public void saveTimetable(TimetableRequest request) {
        String studentId = request.getStudentId();

        // 해당 studentId가 users 테이블에 존재하는지 확인
        Optional<User> userOpt = userRepository.findByUsername(studentId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("해당 studentId는 유효하지 않습니다.");
        }

        // 기존 시간표 삭제
        timetableRepository.deleteByStudentId(studentId);

        // 시간표 저장
        for (TimetableRequest.TimetableRow row : request.getTimetable()) {
            saveCell(studentId, row.getTime(), "월", row.getMon());
            saveCell(studentId, row.getTime(), "화", row.getTue());
            saveCell(studentId, row.getTime(), "수", row.getWed());
            saveCell(studentId, row.getTime(), "목", row.getThu());
            saveCell(studentId, row.getTime(), "금", row.getFri());
        }
    }

    // 시간표 저장 처리
    private void saveCell(String studentId, String time, String day, String subject) {
        // subject가 null이거나 빈 값이면 빈 문자열("")로 처리해서 저장
        if (subject == null || subject.trim().isEmpty()) {
            subject = "";  // null 또는 빈 값은 빈 문자열로 설정
        }
        Timetable tt = new Timetable();
        tt.setStudentId(studentId);
        tt.setDayOfWeek(day);
        tt.setTimeSlot(time);
        tt.setSubject(subject);
        timetableRepository.save(tt);
    }

    // 특정 studentId에 해당하는 시간표 조회
    public List<Timetable> getTimetableByStudentId(String studentId) {
        List<Timetable> timetableList = timetableRepository.findByStudentId(studentId);
        for (Timetable timetable : timetableList) {
            System.out.println("시간표 항목: " + timetable); // toString()이 호출됩니다.
        }
        timetableList.removeIf(timetable -> timetable.getSubject() == null || timetable.getSubject().isEmpty());
        return timetableList;
    }
}

