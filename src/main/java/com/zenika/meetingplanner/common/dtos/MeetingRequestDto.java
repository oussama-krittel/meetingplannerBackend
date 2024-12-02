package com.zenika.meetingplanner.common.dtos;

import com.zenika.meetingplanner.domain.MeetingType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MeetingRequestDto {
    private String meetingType;      // Type of the meeting
    private int participantCount;        // Number of participants
    private String meetingDate;          // Date of the meeting (String format: YYYY-MM-DD)
    private int meetingHour;             // Hour of the meeting (8 to 20)
}
