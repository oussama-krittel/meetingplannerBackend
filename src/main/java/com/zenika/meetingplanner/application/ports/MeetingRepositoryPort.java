package com.zenika.meetingplanner.application.ports;

import com.zenika.meetingplanner.domain.Meeting;

public interface MeetingRepositoryPort {

    // Save a meeting
    Meeting save(Meeting meeting);
}
