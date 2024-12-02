package com.zenika.meetingplanner.common.utils;

import com.zenika.meetingplanner.domain.Equipment;
import com.zenika.meetingplanner.domain.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomSuitabilityComparatorTest {

    private RoomSuitabilityComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new RoomSuitabilityComparator();
    }

    @Test
    void testCompare_SameCapacityAndEquipments_ShouldReturnZero() {
        // Arrange
        Room room1 = Room.builder()
                .name("Room A")
                .capacity(10)
                .equipments(List.of(new Equipment("Whiteboard"), new Equipment("Projector")))
                .build();

        Room room2 = Room.builder()
                .name("Room B")
                .capacity(10)
                .equipments(List.of(new Equipment("Whiteboard"), new Equipment("Projector")))
                .build();

        // Act
        int result = comparator.compare(room1, room2);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void testCompare_DifferentCapacity_ShouldReturnNegative() {
        // Arrange
        Room room1 = Room.builder()
                .name("Room A")
                .capacity(15)
                .equipments(List.of(new Equipment("Whiteboard")))
                .build();

        Room room2 = Room.builder()
                .name("Room B")
                .capacity(20)
                .equipments(List.of(new Equipment("Whiteboard")))
                .build();

        // Act
        int result = comparator.compare(room1, room2);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    void testCompare_DifferentCapacity_ShouldReturnPositive() {
        // Arrange
        Room room1 = Room.builder()
                .name("Room A")
                .capacity(25)
                .equipments(List.of(new Equipment("Whiteboard")))
                .build();

        Room room2 = Room.builder()
                .name("Room B")
                .capacity(20)
                .equipments(List.of(new Equipment("Whiteboard")))
                .build();

        // Act
        int result = comparator.compare(room1, room2);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void testCompare_DifferentEquipments_ShouldReturnNegative() {
        // Arrange
        Room room1 = Room.builder()
                .name("Room A")
                .capacity(10)
                .equipments(List.of(new Equipment("Whiteboard")))
                .build();

        Room room2 = Room.builder()
                .name("Room B")
                .capacity(10)
                .equipments(List.of(new Equipment("Whiteboard"), new Equipment("Projector")))
                .build();

        // Act
        int result = comparator.compare(room1, room2);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    void testCompare_DifferentEquipments_ShouldReturnPositive() {
        // Arrange
        Room room1 = Room.builder()
                .name("Room A")
                .capacity(10)
                .equipments(List.of(new Equipment("Whiteboard"), new Equipment("Projector")))
                .build();

        Room room2 = Room.builder()
                .name("Room B")
                .capacity(10)
                .equipments(List.of(new Equipment("Whiteboard")))
                .build();

        // Act
        int result = comparator.compare(room1, room2);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void testCompare_DifferentCapacityAndEquipments_ShouldHandleComplexCases() {
        // Arrange
        Room room1 = Room.builder()
                .name("Room A")
                .capacity(15)
                .equipments(List.of(new Equipment("Whiteboard"), new Equipment("Projector")))
                .build();

        Room room2 = Room.builder()
                .name("Room B")
                .capacity(20)
                .equipments(List.of(new Equipment("Whiteboard")))
                .build();

        // Act
        int result = comparator.compare(room1, room2);

        // Assert
        // room1: 15 + (3 * 2) = 21
        // room2: 20 + (3 * 1) = 23
        assertEquals(-1, result);
    }

    @Test
    void testCompare_EmptyEquipments_ShouldConsiderCapacityOnly() {
        // Arrange
        Room room1 = Room.builder()
                .name("Room A")
                .capacity(15)
                .equipments(List.of())
                .build();

        Room room2 = Room.builder()
                .name("Room B")
                .capacity(10)
                .equipments(List.of())
                .build();

        // Act
        int result = comparator.compare(room1, room2);

        // Assert
        assertEquals(1, result);
    }
}
