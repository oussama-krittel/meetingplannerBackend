package com.zenika.meetingplanner.adapters.inbound.rest;

import com.zenika.meetingplanner.application.usecases.GetBestRoomWithAvailableHoursUseCase;
import com.zenika.meetingplanner.common.dtos.RoomWithAvailableHoursDto;
import com.zenika.meetingplanner.common.exceptions.NoSuitableRoomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetBestRoomWithAvailableHoursUseCase getBestRoomWithAvailableHoursUseCase;

    private RoomWithAvailableHoursDto roomWithAvailableHoursDto;

    @BeforeEach
    void setUp() {
        // Initialize mock data
        roomWithAvailableHoursDto = RoomWithAvailableHoursDto.builder()
                .name("Conference Room A")
                .capacity(20)
                .roomEquipments(List.of("Whiteboard", "Projector"))
                .availableHours(List.of("8h00-9h00", "10h00-11h00"))
                .build();

        // Mock the use case response
        when(getBestRoomWithAvailableHoursUseCase.execute("SPEC", 10, LocalDate.of(2024, 12, 1)))
                .thenReturn(roomWithAvailableHoursDto);
    }

    @Test
    void testGetBestRoomWithAvailableHours_ShouldReturnRoomDetails() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/rooms/best-room-with-available-hours")
                        .param("meetingType", "SPEC")
                        .param("requiredCapacity", "10")
                        .param("meetingDate", "2024-12-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Conference Room A"))
                .andExpect(jsonPath("$.capacity").value(20))
                .andExpect(jsonPath("$.roomEquipments[0]").value("Whiteboard"))
                .andExpect(jsonPath("$.availableHours[0]").value("8h00-9h00"));

        // Verify that the use case method was called
        verify(getBestRoomWithAvailableHoursUseCase, times(1))
                .execute("SPEC", 10, LocalDate.of(2024, 12, 1));
    }

    @Test
    void testGetBestRoomWithAvailableHours_ShouldReturnNotFound_WhenRoomNotAvailable() throws Exception {
        // Arrange: Make the use case return null or throw an exception
        when(getBestRoomWithAvailableHoursUseCase.execute("SPEC", 100, LocalDate.of(2024, 12, 1)))
                .thenThrow(new NoSuitableRoomException("No suitable room found for the given meeting type and capacity."));

        // Act & Assert
        mockMvc.perform(get("/api/rooms/best-room-with-available-hours")
                        .param("meetingType", "SPEC")
                        .param("requiredCapacity", "100")
                        .param("meetingDate", "2024-12-01")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No suitable room found for the given meeting type and capacity."));

        // Verify that the use case method was called
        verify(getBestRoomWithAvailableHoursUseCase, times(1))
                .execute("SPEC", 100, LocalDate.of(2024, 12, 1));
    }

    @Test
    void testGetBestRoomWithAvailableHours_ShouldReturnBadRequest_WhenInvalidDateFormat() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/rooms/best-room-with-available-hours")
                        .param("meetingType", "SPEC")
                        .param("requiredCapacity", "10")
                        .param("meetingDate", "invalid-date")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Ensure 400 Bad Request status
                .andExpect(content().string("Invalid date format: invalid-date")); // Check the exact response message

        // Verify that no use case method was called
        verify(getBestRoomWithAvailableHoursUseCase, times(0)).execute(anyString(), anyInt(), any(LocalDate.class));
    }
}
