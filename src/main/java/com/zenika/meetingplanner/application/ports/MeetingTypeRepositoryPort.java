package com.zenika.meetingplanner.application.ports;

import com.zenika.meetingplanner.domain.MeetingType;

import java.util.Optional;

public interface MeetingTypeRepositoryPort {

    // Find meeting type by name
    Optional<MeetingType> findByName(String meetingTypeName);
}
