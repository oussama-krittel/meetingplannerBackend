package com.zenika.meetingplanner.application.usecases;

import com.zenika.meetingplanner.application.ports.MeetingTypeRepositoryPort;
import com.zenika.meetingplanner.application.ports.RoomRepositoryPort;
import com.zenika.meetingplanner.common.dtos.RoomWithAvailableHoursDto;
import com.zenika.meetingplanner.common.exceptions.MeetingTypeNotFoundException;
import com.zenika.meetingplanner.common.exceptions.NoSuitableRoomException;
import com.zenika.meetingplanner.common.utils.RoomSuitabilityComparator;
import com.zenika.meetingplanner.domain.Equipment;
import com.zenika.meetingplanner.domain.MeetingType;
import com.zenika.meetingplanner.domain.Room;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GetBestRoomWithAvailableHoursUseCase {

    private final RoomRepositoryPort roomRepository;

    private final MeetingTypeRepositoryPort meetingTypeRepositoryPort;

    public GetBestRoomWithAvailableHoursUseCase(RoomRepositoryPort roomRepository, MeetingTypeRepositoryPort meetingTypeRepositoryPort) {
        this.roomRepository = roomRepository;
        this.meetingTypeRepositoryPort = meetingTypeRepositoryPort;
    }

    /**
     * Finds the best room for a given meeting type and capacity and returns its information
     * along with the available hours.
     *
     * @param meetingTypeName  The type of meeting (defines equipment requirements).
     * @param requiredCapacity The required capacity for the meeting.
     * @param meetingDate      The date of meeting .
     * @return A DTO containing the best room's information and its available hours.
     */
    public RoomWithAvailableHoursDto execute(String meetingTypeName, int requiredCapacity, LocalDate meetingDate) {
        // Convert meetingType
        Optional<MeetingType> meetingTypeOptional = meetingTypeRepositoryPort.findByName(meetingTypeName);

        // If not present, throw a custom exception
        meetingTypeOptional.orElseThrow(() -> new MeetingTypeNotFoundException(meetingTypeName));

        // Get the MeetingType
        MeetingType meetingType = meetingTypeOptional.get();

        // Fetch all rooms from the repository
        List<Room> rooms = roomRepository.findAllRooms();

        // Find the best room based on suitability
        Optional<Room> bestRoom = rooms.stream()
                .filter(room -> room.isSuitableForMeetingType(meetingType) && room.hasCapacity(requiredCapacity)
                        && !room.findAvailableHoursOnDate(meetingDate).isEmpty())
                .min(new RoomSuitabilityComparator());

        // If no suitable room is found, throw an exception
        if (bestRoom.isEmpty()) {
            throw new NoSuitableRoomException("No suitable room found for the given meeting type and capacity.");
        }

        // Get the best room
        Room room = bestRoom.get();

        // Fetch the available hours
        List<String> availableHours = room.findAvailableHoursOnDate(meetingDate).stream()
                .map(time -> time + "h00-" + (time+1) + "h00") // Convert time to appropriate representation
                .toList();

        // Return the room info and available hours as a DTO
        return RoomWithAvailableHoursDto.builder()
                .name(room.getName())
                .capacity(room.getCapacity())
                .roomEquipments(room.getEquipments().stream().map(Equipment::getName).toList())
                .availableHours(availableHours)
                .build();
    }
}
