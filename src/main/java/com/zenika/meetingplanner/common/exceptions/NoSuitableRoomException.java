package com.zenika.meetingplanner.common.exceptions;

public class NoSuitableRoomException extends RuntimeException {

    public NoSuitableRoomException(String message) {
        super(message);
    }

    public NoSuitableRoomException(String message, Throwable cause) {
        super(message, cause);
    }
}
