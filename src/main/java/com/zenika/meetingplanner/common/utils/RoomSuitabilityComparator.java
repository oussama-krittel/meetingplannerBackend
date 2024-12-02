package com.zenika.meetingplanner.common.utils;

import com.zenika.meetingplanner.domain.MeetingType;
import com.zenika.meetingplanner.domain.Room;

import java.util.Comparator;

public class RoomSuitabilityComparator implements Comparator<Room> {

    public RoomSuitabilityComparator() {
    }

    @Override
    public int compare(Room room1, Room room2) {
        // Compare based on capacity difference and number of equipments
        int room1Score = room1.getCapacity() + 3 * room1.getEquipments().size();
        int room2Score = room2.getCapacity() + 3 * room2.getEquipments().size();

        return Integer.compare(room1Score, room2Score);
    }
}

