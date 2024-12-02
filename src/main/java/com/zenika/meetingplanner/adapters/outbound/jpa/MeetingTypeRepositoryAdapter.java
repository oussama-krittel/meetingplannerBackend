package com.zenika.meetingplanner.adapters.outbound.jpa;

import com.zenika.meetingplanner.adapters.outbound.jpa.entities.JpaMeetingType;
import com.zenika.meetingplanner.adapters.outbound.jpa.repositories.JpaMeetingTypeRepository;
import com.zenika.meetingplanner.application.ports.MeetingTypeRepositoryPort;
import com.zenika.meetingplanner.domain.MeetingType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MeetingTypeRepositoryAdapter implements MeetingTypeRepositoryPort {

    private final JpaMeetingTypeRepository jpaMeetingTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MeetingTypeRepositoryAdapter(JpaMeetingTypeRepository jpaMeetingTypeRepository, ModelMapper modelMapper) {
        this.jpaMeetingTypeRepository = jpaMeetingTypeRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Finds a meeting type by its name and maps it to a domain object.
     *
     * @param meetingTypeName The name of the meeting type to search for.
     * @return An Optional containing the MeetingType domain model.
     */
    @Override
    public Optional<MeetingType> findByName(String meetingTypeName) {
        // Find the JpaMeetingType entity by name
        Optional<JpaMeetingType> jpaMeetingType = jpaMeetingTypeRepository.findByName(meetingTypeName);

        // If the entity is present, map it to the MeetingType domain model using ModelMapper
        return jpaMeetingType.map(meetingType -> modelMapper.map(meetingType, MeetingType.class));
    }
}
