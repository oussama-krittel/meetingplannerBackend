package com.zenika.meetingplanner.application.usecases;

import com.zenika.meetingplanner.application.ports.MeetingRepositoryPort;
import com.zenika.meetingplanner.application.ports.MeetingTypeRepositoryPort;
import com.zenika.meetingplanner.application.ports.RoomRepositoryPort;
import com.zenika.meetingplanner.common.dtos.MeetingRequestDto;
import com.zenika.meetingplanner.common.dtos.MeetingResponseDto;
import com.zenika.meetingplanner.common.exceptions.InvalidMeetingHourException;
import com.zenika.meetingplanner.common.exceptions.MeetingTypeNotFoundException;
import com.zenika.meetingplanner.common.exceptions.NoSuitableRoomException;
import com.zenika.meetingplanner.common.utils.RoomSuitabilityComparator;
import com.zenika.meetingplanner.domain.Meeting;
import com.zenika.meetingplanner.domain.MeetingType;
import com.zenika.meetingplanner.domain.Room;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AssignMeetingToBestRoomUseCase {

    private final RoomRepositoryPort roomRepository;
    private final MeetingRepositoryPort meetingRepository;
    private final MeetingTypeRepositoryPort meetingTypeRepositoryPort;


    public AssignMeetingToBestRoomUseCase(RoomRepositoryPort roomRepository, MeetingRepositoryPort meetingRepository, MeetingTypeRepositoryPort meetingTypeRepositoryPort) {
        this.roomRepository = roomRepository;
        this.meetingRepository = meetingRepository;
        this.meetingTypeRepositoryPort = meetingTypeRepositoryPort;
    }

    /**
     * Assigns a meeting to the best available room and saves it.
     *
     * @param requestDto The meeting creation request details.
     * @return A response DTO with the saved meeting details.
     */
    public MeetingResponseDto execute(MeetingRequestDto requestDto) {
        // Parse meeting date
        LocalDate meetingDate = LocalDate.parse(requestDto.getMeetingDate());

        //validate Hour
        if (requestDto.getMeetingHour() < 8 || requestDto.getMeetingHour() > 20) {
            throw new InvalidMeetingHourException("Meeting hour must be between 8 and 20.");
        }

        // Convert meetingType
        Optional<MeetingType> meetingTypeOptional = meetingTypeRepositoryPort.findByName(requestDto.getMeetingType());

        // If not present, throw a custom exception
        meetingTypeOptional.orElseThrow(() -> new MeetingTypeNotFoundException(requestDto.getMeetingType()));

        // Get the MeetingType
        MeetingType meetingType = meetingTypeOptional.get();

        // Fetch all rooms
        List<Room> rooms = roomRepository.findAllRooms();

        // Find the best room
        Optional<Room> bestRoom = rooms.stream()
                .filter(room -> room.isSuitableForMeetingType(meetingType)
                        && room.hasCapacity(requestDto.getParticipantCount())
                        && room.isAvailableAt(meetingDate, requestDto.getMeetingHour()))
                .min(new RoomSuitabilityComparator());

        if (bestRoom.isEmpty()) {
            throw new NoSuitableRoomException("No suitable room found for the given meeting type, capacity, and time.");
        }

        // Create the meeting
        Room assignedRoom = bestRoom.get();
        Meeting meeting = Meeting.builder()
                .type(meetingType)
                .participantCount(requestDto.getParticipantCount())
                .date(meetingDate)
                .hour(requestDto.getMeetingHour())
                .room(assignedRoom)
                .build();

        // Save the meeting
        Meeting savedMeeting = meetingRepository.save(meeting);

        System.out.println(savedMeeting.getRoom().getName());

        // Return response DTO
        return MeetingResponseDto.builder()
                .meetingType(savedMeeting.getType().getName())
                .participantCount(savedMeeting.getParticipantCount())
                .meetingDate(savedMeeting.getDate().toString())
                .meetingHour(savedMeeting.getHour())
                .assignedRoomName(savedMeeting.getRoom().getName())
                .build();
    }
}
