package com.zenika.meetingplanner.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private Long id;
    private String name;
    private int capacity;
    private List<Meeting> reservations = new ArrayList<>();
    private List<Equipment> equipments = new ArrayList<>();

    /**
     * Checks if the room is available at a given date and hour.
     *
     * @param date The date to check availability.
     * @param hour The hour to check availability (e.g., 8, 9, 10).
     * @return True if the room is available, false otherwise.
     */
    public boolean isAvailableAt(LocalDate date, int hour) {
        return reservations.stream().noneMatch(meeting -> meeting.getDate().isEqual(date)
                && (meeting.getHour() == hour || meeting.getHour() == hour + 1 || meeting.getHour() == hour - 1));
    }

    /**
     * Checks if the room contains all the required equipment.
     *
     * @param requiredEquipment The list of required equipment.
     * @return True if the room contains all the required equipment, false otherwise.
     */
    public boolean hasAllRequiredEquipment(List<Equipment> requiredEquipment) {
        return new HashSet<>(equipments).containsAll(requiredEquipment);
    }

    /**
     * Checks if the room meets the capacity requirement.
     *
     * @param requiredCapacity The required capacity for the meeting.
     * @return True if the room can accommodate the required capacity, false otherwise.
     */
    public boolean hasCapacity(int requiredCapacity) {
        return capacity * 0.7 >= requiredCapacity; // 70% capacity rule for COVID
    }

    /**
     * Checks if the room is suitable for a specific meeting type.
     *
     * @param meetingType The meeting type to check.
     * @return True if the room is suitable for the meeting type, false otherwise.
     */
    public boolean isSuitableForMeetingType(MeetingType meetingType) {
        return hasAllRequiredEquipment(meetingType.getRequiredEquipment())
                && meetingType.isCapacitySatisfied(this.getCapacity());
    }

    /**
     * Finds the next available hours for the room.
     *
     * @param date The date to check for availability.
     * @return A list of available time slots in the format of ReservationKey.
     */
    public List<Integer> findAvailableHoursOnDate(LocalDate date) {
        List<Integer> availableHours = new ArrayList<>();

        for (int hour = 8; hour < 20; hour++) { // Iterate from 8:00 to 20:00
            if (isAvailableAt(date, hour)) {
                availableHours.add(hour); // Add available hours to the list
            }
        }

        return availableHours;
    }

}
