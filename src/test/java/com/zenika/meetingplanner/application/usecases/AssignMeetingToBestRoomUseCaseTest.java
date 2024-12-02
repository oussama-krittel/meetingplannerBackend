package com.zenika.meetingplanner.application.usecases;

import com.zenika.meetingplanner.application.ports.MeetingRepositoryPort;
import com.zenika.meetingplanner.application.ports.MeetingTypeRepositoryPort;
import com.zenika.meetingplanner.application.ports.RoomRepositoryPort;
import com.zenika.meetingplanner.common.dtos.MeetingRequestDto;
import com.zenika.meetingplanner.common.dtos.MeetingResponseDto;
import com.zenika.meetingplanner.common.exceptions.InvalidMeetingHourException;
import com.zenika.meetingplanner.common.exceptions.MeetingTypeNotFoundException;
import com.zenika.meetingplanner.common.exceptions.NoSuitableRoomException;
import com.zenika.meetingplanner.domain.Equipment;
import com.zenika.meetingplanner.domain.Meeting;
import com.zenika.meetingplanner.domain.MeetingType;
import com.zenika.meetingplanner.domain.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssignMeetingToBestRoomUseCaseTest {

    @InjectMocks
    private AssignMeetingToBestRoomUseCase useCase;

    @Mock
    private RoomRepositoryPort roomRepositoryPort;

    @Mock
    private MeetingRepositoryPort meetingRepositoryPort;

    @Mock
    private MeetingTypeRepositoryPort meetingTypeRepositoryPort;

    private MeetingRequestDto requestDto;
    private MeetingType meetingType;
    private Room room;
    private Meeting savedMeeting;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock MeetingType
        meetingType = MeetingType.builder()
                .name("SPEC")
                .minimumCapacity(5)
                .requiredEquipment(List.of(new Equipment("Whiteboard")))
                .build();

        // Mock Room
        room = Room.builder()
                .name("Conference Room A")
                .capacity(12)
                .equipments(List.of(new Equipment("Whiteboard")))
                .reservations(List.of())
                .build();

        // Mock MeetingRequestDto
        requestDto = MeetingRequestDto.builder()
                .meetingType("SPEC")
                .participantCount(8)
                .meetingDate("2024-12-01")
                .meetingHour(10)
                .build();

        // Mock Saved Meeting
        savedMeeting = Meeting.builder()
                .type(meetingType)
                .participantCount(8)
                .date(LocalDate.of(2024, 12, 1))
                .hour(10)
                .room(room)
                .build();
    }

    @Test
    void testExecute_ShouldAssignMeetingToBestRoom() {
        // Arrange
        when(meetingTypeRepositoryPort.findByName("SPEC")).thenReturn(Optional.of(meetingType));
        when(roomRepositoryPort.findAllRooms()).thenReturn(List.of(room));
        when(meetingRepositoryPort.save(any(Meeting.class))).thenReturn(savedMeeting);

        // Act
        MeetingResponseDto response = useCase.execute(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals("SPEC", response.getMeetingType());
        assertEquals(8, response.getParticipantCount());
        assertEquals("2024-12-01", response.getMeetingDate());
        assertEquals(10, response.getMeetingHour());
        assertEquals("Conference Room A", response.getAssignedRoomName());

        // Verify interactions
        verify(meetingTypeRepositoryPort, times(1)).findByName("SPEC");
        verify(roomRepositoryPort, times(1)).findAllRooms();
        verify(meetingRepositoryPort, times(1)).save(any(Meeting.class));
    }

    @Test
    void testExecute_ShouldThrowMeetingTypeNotFoundException() {
        // Arrange
        when(meetingTypeRepositoryPort.findByName("SPEC")).thenReturn(Optional.empty());

        // Act & Assert
        MeetingTypeNotFoundException exception = assertThrows(MeetingTypeNotFoundException.class,
                () -> useCase.execute(requestDto));

        assertEquals("Meeting type 'SPEC' not found.", exception.getMessage());

        // Verify interactions
        verify(meetingTypeRepositoryPort, times(1)).findByName("SPEC");
        verify(roomRepositoryPort, never()).findAllRooms();
        verify(meetingRepositoryPort, never()).save(any(Meeting.class));
    }

    @Test
    void testExecute_ShouldThrowInvalidMeetingHourException() {
        // Arrange
        requestDto.setMeetingHour(22); // Invalid hour

        // Act & Assert
        InvalidMeetingHourException exception = assertThrows(InvalidMeetingHourException.class,
                () -> useCase.execute(requestDto));

        assertEquals("Meeting hour must be between 8 and 20.", exception.getMessage());

        // Verify no interactions with repositories
        verify(meetingTypeRepositoryPort, never()).findByName(anyString());
        verify(roomRepositoryPort, never()).findAllRooms();
        verify(meetingRepositoryPort, never()).save(any(Meeting.class));
    }

    @Test
    void testExecute_ShouldThrowNoSuitableRoomException() {
        // Arrange
        when(meetingTypeRepositoryPort.findByName("SPEC")).thenReturn(Optional.of(meetingType));
        when(roomRepositoryPort.findAllRooms()).thenReturn(List.of()); // No rooms available

        // Act & Assert
        NoSuitableRoomException exception = assertThrows(NoSuitableRoomException.class,
                () -> useCase.execute(requestDto));

        assertEquals("No suitable room found for the given meeting type, capacity, and time.", exception.getMessage());

        // Verify interactions
        verify(meetingTypeRepositoryPort, times(1)).findByName("SPEC");
        verify(roomRepositoryPort, times(1)).findAllRooms();
        verify(meetingRepositoryPort, never()).save(any(Meeting.class));
    }
}
