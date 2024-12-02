package com.zenika.meetingplanner.common.exceptions;

/**
 * Exception thrown when a requested MeetingType is not found.
 */
public class MeetingTypeNotFoundException extends RuntimeException {

    public MeetingTypeNotFoundException(String meetingTypeName) {
        super("Meeting type '" + meetingTypeName + "' not found.");
    }

    public MeetingTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
