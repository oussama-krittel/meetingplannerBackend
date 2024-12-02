package com.zenika.meetingplanner.adapters.outbound.jpa;

import com.zenika.meetingplanner.adapters.outbound.jpa.entities.JpaRoom;
import com.zenika.meetingplanner.adapters.outbound.jpa.repositories.JpaRoomRepository;
import com.zenika.meetingplanner.application.ports.RoomRepositoryPort;
import com.zenika.meetingplanner.domain.Room;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomRepositoryAdapter implements RoomRepositoryPort {

    private final JpaRoomRepository jpaRoomRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoomRepositoryAdapter(JpaRoomRepository jpaRoomRepository, ModelMapper modelMapper) {
        this.jpaRoomRepository = jpaRoomRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Room> findAllRooms() {
        // Fetch all JpaRoom entities
        List<JpaRoom> jpaRooms = jpaRoomRepository.findAll();

        // Map each JpaRoom to Room using ModelMapper
        return jpaRooms.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    /**
     * Converts a JpaRoom entity to a Room domain model using ModelMapper.
     *
     * @param jpaRoom The JpaRoom entity.
     * @return The corresponding Room domain model.
     */
    private Room convertToDomain(JpaRoom jpaRoom) {
        return modelMapper.map(jpaRoom, Room.class);
    }
}
