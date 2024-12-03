package com.zenika.meetingplanner.adapters.inbound.rest;

import com.zenika.meetingplanner.application.usecases.AssignMeetingToBestRoomUseCase;
import com.zenika.meetingplanner.common.dtos.MeetingRequestDto;
import com.zenika.meetingplanner.common.dtos.MeetingResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    private final AssignMeetingToBestRoomUseCase assignMeetingToBestRoomUseCase;

    @Autowired
    public MeetingController(AssignMeetingToBestRoomUseCase assignMeetingToBestRoomUseCase) {
        this.assignMeetingToBestRoomUseCase = assignMeetingToBestRoomUseCase;
    }

    /**
     * Endpoint to assign a meeting to the best available room and save it.
     *
     * @param requestDto The meeting creation request details.
     * @return The saved meeting's details in a response DTO.
     */
    @PostMapping("/assign-to-best-room")
    public ResponseEntity<MeetingResponseDto> assignMeetingToBestRoom(@RequestBody MeetingRequestDto requestDto) {
        return ResponseEntity.ok().body(
                assignMeetingToBestRoomUseCase.execute(requestDto));
    }
}
