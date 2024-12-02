package com.zenika.meetingplanner.common.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MeetingResponseDto {
    private String meetingType;          // Type of the meeting
    private int participantCount;        // Number of participants
    private String meetingDate;          // Date of the meeting (String format)
    private int meetingHour;             // Hour of the meeting
    private String assignedRoomName;     // Name of the assigned room
}
