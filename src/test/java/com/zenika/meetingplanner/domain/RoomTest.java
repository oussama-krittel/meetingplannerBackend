package com.zenika.meetingplanner.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private Room room;
    private MeetingType meetingType;
    private Equipment projector;
    private Equipment whiteboard;

    @BeforeEach
    void setUp() {
        projector = new Equipment("Projector");
        whiteboard = new Equipment("Whiteboard");

        room = Room.builder()
                .name("Conference Room A")
                .capacity(20)
                .reservations(List.of(
                        Meeting.builder().date(LocalDate.of(2024, 12, 1)).hour(10).build(),
                        Meeting.builder().date(LocalDate.of(2024, 12, 1)).hour(15).build()
                ))
                .equipments(List.of(projector, whiteboard))
                .build();

        meetingType = MeetingType.builder()
                .name("SPEC")
                .minimumCapacity(5)
                .requiredEquipment(List.of(whiteboard))
                .build();
    }

    @Test
    void testIsAvailableAt_WhenRoomIsAvailable_ShouldReturnTrue() {
        LocalDate date = LocalDate.of(2024, 12, 1);
        int hour = 12; // No reservation at 12:00 or surrounding hours
        assertTrue(room.isAvailableAt(date, hour));
    }

    @Test
    void testIsAvailableAt_WhenRoomIsNotAvailable_ShouldReturnFalse() {
        LocalDate date = LocalDate.of(2024, 12, 1);
        int hour = 10; // Reservation exists at 10:00
        assertFalse(room.isAvailableAt(date, hour));
    }

    @Test
    void testHasAllRequiredEquipment_WhenRoomHasAllRequiredEquipment_ShouldReturnTrue() {
        List<Equipment> requiredEquipment = List.of(whiteboard);
        assertTrue(room.hasAllRequiredEquipment(requiredEquipment));
    }

    @Test
    void testHasAllRequiredEquipment_WhenRoomDoesNotHaveAllRequiredEquipment_ShouldReturnFalse() {
        List<Equipment> requiredEquipment = List.of(new Equipment("Microphone"));
        assertFalse(room.hasAllRequiredEquipment(requiredEquipment));
    }

    @Test
    void testHasCapacity_WhenRoomMeetsCapacityRequirement_ShouldReturnTrue() {
        int requiredCapacity = 14; // 70% of 20 is 14
        assertTrue(room.hasCapacity(requiredCapacity));
    }

    @Test
    void testHasCapacity_WhenRoomDoesNotMeetCapacityRequirement_ShouldReturnFalse() {
        int requiredCapacity = 15; // Exceeds 70% of capacity
        assertFalse(room.hasCapacity(requiredCapacity));
    }

    @Test
    void testIsSuitableForMeetingType_WhenRoomIsSuitable_ShouldReturnTrue() {
        assertTrue(room.isSuitableForMeetingType(meetingType));
    }

    @Test
    void testIsSuitableForMeetingType_WhenRoomIsNotSuitable_ShouldReturnFalse() {
        MeetingType meetingTypeWithHighCapacity = MeetingType.builder()
                .name("Large Meeting")
                .minimumCapacity(25) // Exceeds room's capacity
                .requiredEquipment(List.of(whiteboard))
                .build();

        assertFalse(room.isSuitableForMeetingType(meetingTypeWithHighCapacity));
    }

    @Test
    void testFindAvailableHoursOnDate_WhenRoomHasFreeSlots_ShouldReturnAvailableHours() {
        LocalDate date = LocalDate.of(2024, 12, 1);
        List<Integer> availableHours = room.findAvailableHoursOnDate(date);

        assertEquals(List.of(8, 12, 13, 17, 18, 19), availableHours);
    }

    @Test
    void testFindAvailableHoursOnDate_WhenRoomHasNoFreeSlots_ShouldReturnEmptyList() {
        room.setReservations(List.of(
                Meeting.builder().date(LocalDate.of(2024, 12, 1)).hour(8).build(),
                Meeting.builder().date(LocalDate.of(2024, 12, 1)).hour(10).build(),
                Meeting.builder().date(LocalDate.of(2024, 12, 1)).hour(12).build(),
                Meeting.builder().date(LocalDate.of(2024, 12, 1)).hour(14).build(),
                Meeting.builder().date(LocalDate.of(2024, 12, 1)).hour(16).build(),
                Meeting.builder().date(LocalDate.of(2024, 12, 1)).hour(18).build()
        ));

        List<Integer> availableHours = room.findAvailableHoursOnDate(LocalDate.of(2024, 12, 1));
        assertTrue(availableHours.isEmpty());
    }
}
