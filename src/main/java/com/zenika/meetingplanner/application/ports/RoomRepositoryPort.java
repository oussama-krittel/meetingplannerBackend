package com.zenika.meetingplanner.application.ports;

import com.zenika.meetingplanner.domain.Room;

import java.util.List;

public interface RoomRepositoryPort {

    // Get all rooms
    List<Room> findAllRooms();
}
