package com.zenika.meetingplanner.adapters.inbound.rest;

import com.zenika.meetingplanner.application.usecases.GetBestRoomWithAvailableHoursUseCase;
import com.zenika.meetingplanner.common.dtos.RoomWithAvailableHoursDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final GetBestRoomWithAvailableHoursUseCase getBestRoomWithAvailableHoursUseCase;

    @Autowired
    public RoomController(GetBestRoomWithAvailableHoursUseCase getBestRoomWithAvailableHoursUseCase) {
        this.getBestRoomWithAvailableHoursUseCase = getBestRoomWithAvailableHoursUseCase;
    }

    /**
     * Endpoint to get the best room for a given meeting type and capacity, along with available hours.
     *
     * @param meetingType      The meeting type (e.g., VIDEO_CONFERENCE).
     * @param requiredCapacity The required capacity for the meeting.
     * @param meetingDate      The date for which to check room availability.
     * @return RoomWithAvailableHoursDto containing the best room and its available hours.
     */
    @GetMapping("/best-room-with-available-hours")
    public ResponseEntity<RoomWithAvailableHoursDto> getBestRoomWithAvailableHours(
            @RequestParam String meetingType,
            @RequestParam int requiredCapacity,
            @RequestParam String meetingDate) {

        // Convert meetingDate from String to LocalDate
        LocalDate date = LocalDate.parse(meetingDate);

        // Call the use case and return the result
        return ResponseEntity.ok().body(
                getBestRoomWithAvailableHoursUseCase.execute(meetingType, requiredCapacity, date));
    }
}
