package com.zenika.meetingplanner.common.exceptions;

public class InvalidMeetingHourException extends RuntimeException {

    public InvalidMeetingHourException(String message) {
        super(message);
    }

    public InvalidMeetingHourException(String message, Throwable cause) {
        super(message, cause);
    }
}
