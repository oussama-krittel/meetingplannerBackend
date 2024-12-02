package com.zenika.meetingplanner.adapters.outbound.jpa;

import com.zenika.meetingplanner.adapters.outbound.jpa.entities.JpaRoom;
import com.zenika.meetingplanner.adapters.outbound.jpa.repositories.JpaRoomRepository;
import com.zenika.meetingplanner.domain.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomRepositoryAdapterTest {

    @Mock
    private JpaRoomRepository jpaRoomRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoomRepositoryAdapter roomRepositoryAdapter;

    private JpaRoom jpaRoom;
    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        jpaRoom = new JpaRoom();
        jpaRoom.setName("Conference Room A");
        jpaRoom.setCapacity(20);

        room = new Room("Conference Room A", 20, null, null);
    }

    @Test
    void testFindAllRooms_ShouldReturnMappedRooms() {
        // Arrange
        when(jpaRoomRepository.findAll()).thenReturn(Collections.singletonList(jpaRoom));
        when(modelMapper.map(jpaRoom, Room.class)).thenReturn(room);

        // Act
        List<Room> rooms = roomRepositoryAdapter.findAllRooms();

        // Assert
        assertNotNull(rooms);
        assertEquals(1, rooms.size());
        assertEquals("Conference Room A", rooms.get(0).getName());
        assertEquals(20, rooms.get(0).getCapacity());

        // Verify interactions
        verify(jpaRoomRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(jpaRoom, Room.class);
    }

    @Test
    void testFindAllRooms_ShouldReturnEmptyList_WhenNoRoomsAvailable() {
        // Arrange
        when(jpaRoomRepository.findAll()).thenReturn(List.of());

        // Act
        List<Room> rooms = roomRepositoryAdapter.findAllRooms();

        // Assert
        assertNotNull(rooms);
        assertTrue(rooms.isEmpty());

        // Verify interaction
        verify(jpaRoomRepository, times(1)).findAll();
        verifyNoInteractions(modelMapper);
    }

}
