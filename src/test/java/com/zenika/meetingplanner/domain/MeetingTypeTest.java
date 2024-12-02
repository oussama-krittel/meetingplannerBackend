package com.zenika.meetingplanner.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeetingTypeTest {

    private MeetingType meetingType;
    private Equipment projector;
    private Equipment whiteboard;

    @BeforeEach
    void setUp() {
        projector = new Equipment("Projector");
        whiteboard = new Equipment("Whiteboard");

        meetingType = MeetingType.builder()
                .name("SPEC") // Example: Specific Meeting Type
                .minimumCapacity(5) // Minimum capacity for this type
                .requiredEquipment(List.of(projector, whiteboard))
                .build();
    }

    @Test
    void testIsCapacitySatisfied_WhenRoomCapacityMeetsRequirement_ShouldReturnTrue() {
        int roomCapacity = 10; // Room capacity greater than minimum required
        assertTrue(meetingType.isCapacitySatisfied(roomCapacity));
    }

    @Test
    void testIsCapacitySatisfied_WhenRoomCapacityEqualsMinimumRequirement_ShouldReturnTrue() {
        int roomCapacity = 5; // Room capacity equal to minimum required
        assertTrue(meetingType.isCapacitySatisfied(roomCapacity));
    }

    @Test
    void testIsCapacitySatisfied_WhenRoomCapacityBelowRequirement_ShouldReturnFalse() {
        int roomCapacity = 3; // Room capacity less than minimum required
        assertFalse(meetingType.isCapacitySatisfied(roomCapacity));
    }
}
