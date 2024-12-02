package com.zenika.meetingplanner.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MeetingType {
    private String name;
    private int minimumCapacity;
    private List<Equipment> requiredEquipment;

    /**
     * Checks if the room's capacity satisfies the minimum capacity required for this meeting type.
     *
     * @param roomCapacity The capacity of the room.
     * @return True if the room's capacity meets the minimum capacity, false otherwise.
     */
    public boolean isCapacitySatisfied(int roomCapacity) {
        return roomCapacity >= minimumCapacity;
    }
}

