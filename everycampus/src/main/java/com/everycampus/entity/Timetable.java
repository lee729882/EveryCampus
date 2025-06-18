package com.everycampus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Timetable {
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Oracle 12c 이상에서 사용 가능
    private Long id;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @Column(name = "time_slot", nullable = false)
    private String timeSlot;

    @Column(name = "subject", nullable = false)  // DB에서 null 값을 허용하지 않음
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_code")
    private Lecture lecture;

    
    // 생성자 추가 (DTO를 엔티티로 변환)
    public Timetable(String studentId, String dayOfWeek, String timeSlot, String subject) {
        this.studentId = studentId;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.subject = subject;
    }
    @Override
    public String toString() {
        return "Timetable{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
