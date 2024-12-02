package com.zenika.meetingplanner.adapters.inbound.rest;

import com.zenika.meetingplanner.application.usecases.AssignMeetingToBestRoomUseCase;
import com.zenika.meetingplanner.common.dtos.MeetingRequestDto;
import com.zenika.meetingplanner.common.dtos.MeetingResponseDto;
import com.zenika.meetingplanner.common.exceptions.InvalidMeetingHourException;
import com.zenika.meetingplanner.common.exceptions.NoSuitableRoomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeetingController.class)
class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssignMeetingToBestRoomUseCase assignMeetingToBestRoomUseCase;

    private MeetingRequestDto requestDto;
    private MeetingResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // Mock input data
        requestDto = MeetingRequestDto.builder()
                .meetingType("SPEC")
                .participantCount(10)
                .meetingDate("2024-12-01")
                .meetingHour(10)
                .build();

        // Mock output data
        responseDto = MeetingResponseDto.builder()
                .meetingType("SPEC")
                .participantCount(10)
                .meetingDate("2024-12-01")
                .meetingHour(10)
                .assignedRoomName("Conference Room A")
                .build();
    }

    @Test
    void testAssignMeetingToBestRoom_ShouldReturnMeetingResponseDto() throws Exception {
        // Arrange
        when(assignMeetingToBestRoomUseCase.execute(any(MeetingRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/meetings/assign-to-best-room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "meetingType": "SPEC",
                          "participantCount": 10,
                          "meetingDate": "2024-12-01",
                          "meetingHour": 10
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetingType").value("SPEC"))
                .andExpect(jsonPath("$.participantCount").value(10))
                .andExpect(jsonPath("$.meetingDate").value("2024-12-01"))
                .andExpect(jsonPath("$.meetingHour").value(10))
                .andExpect(jsonPath("$.assignedRoomName").value("Conference Room A"));

        // Verify interactions
        verify(assignMeetingToBestRoomUseCase, times(1)).execute(any(MeetingRequestDto.class));
    }

    @Test
    void testAssignMeetingToBestRoom_ShouldReturnBadRequest_WhenInvalidMeetingHour() throws Exception {
        // Arrange
        doThrow(new InvalidMeetingHourException("Meeting hour must be between 8 and 20."))
                .when(assignMeetingToBestRoomUseCase).execute(any(MeetingRequestDto.class));

        // Act & Assert
        mockMvc.perform(post("/api/meetings/assign-to-best-room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "meetingType": "SPEC",
                          "participantCount": 10,
                          "meetingDate": "2024-12-01",
                          "meetingHour": 22
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Meeting hour must be between 8 and 20."));

        // Verify interactions
        verify(assignMeetingToBestRoomUseCase, times(1)).execute(any(MeetingRequestDto.class));
    }

    @Test
    void testAssignMeetingToBestRoom_ShouldReturnNotFound_WhenNoSuitableRoomAvailable() throws Exception {
        // Arrange
        doThrow(new NoSuitableRoomException("No suitable room found for the given meeting type, capacity, and time."))
                .when(assignMeetingToBestRoomUseCase).execute(any(MeetingRequestDto.class));

        // Act & Assert
        mockMvc.perform(post("/api/meetings/assign-to-best-room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "meetingType": "SPEC",
                          "participantCount": 100,
                          "meetingDate": "2024-12-01",
                          "meetingHour": 10
                        }
                        """))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No suitable room found for the given meeting type, capacity, and time."));

        // Verify interactions
        verify(assignMeetingToBestRoomUseCase, times(1)).execute(any(MeetingRequestDto.class));
    }
}
