package com.everycampus.dto;

import lombok.Data;
import java.util.List;

@Data
public class TimetableRequest {
    private String studentId;
    private List<TimetableRow> timetable;

    @Data
    public static class TimetableRow {
        private String time;
        private String mon;
        private String tue;
        private String wed;
        private String thu;
        private String fri;
    }
}
