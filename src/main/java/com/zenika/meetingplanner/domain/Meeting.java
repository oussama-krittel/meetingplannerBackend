package com.zenika.meetingplanner.domain;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Meeting {
    private int order;
    private LocalDate date; // Stores the date of the meeting
    private int hour;       // Stores the hour (e.g., 8, 9, 10)
    private MeetingType type;
    private int participantCount;
    private Room room;
}

