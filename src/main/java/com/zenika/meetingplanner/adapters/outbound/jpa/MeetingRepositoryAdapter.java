package com.zenika.meetingplanner.adapters.outbound.jpa;

import com.zenika.meetingplanner.adapters.outbound.jpa.entities.JpaMeeting;
import com.zenika.meetingplanner.adapters.outbound.jpa.repositories.JpaMeetingRepository;
import com.zenika.meetingplanner.application.ports.MeetingRepositoryPort;
import com.zenika.meetingplanner.domain.Meeting;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingRepositoryAdapter implements MeetingRepositoryPort {

    private final JpaMeetingRepository jpaMeetingRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MeetingRepositoryAdapter(JpaMeetingRepository jpaMeetingRepository, ModelMapper modelMapper) {
        this.jpaMeetingRepository = jpaMeetingRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Saves a Meeting domain model as a JpaMeeting entity in the database.
     *
     * @param meeting The Meeting domain model to save.
     * @return The saved Meeting domain model.
     */
    @Override
    public Meeting save(Meeting meeting) {
        // Convert domain Meeting to JPA entity
        JpaMeeting jpaMeeting = convertToEntity(meeting);

        // Save the JPA entity
        JpaMeeting savedJpaMeeting = jpaMeetingRepository.save(jpaMeeting);

        // Convert saved JPA entity back to domain Meeting
        return convertToDomain(savedJpaMeeting);
    }

    /**
     * Converts a Meeting domain model to a JpaMeeting entity using ModelMapper.
     *
     * @param meeting The domain model to convert.
     * @return The corresponding JpaMeeting entity.
     */
    private JpaMeeting convertToEntity(Meeting meeting) {
        return modelMapper.map(meeting, JpaMeeting.class);
    }

    /**
     * Converts a JpaMeeting entity to a Meeting domain model using ModelMapper.
     *
     * @param jpaMeeting The JPA entity to convert.
     * @return The corresponding Meeting domain model.
     */
    private Meeting convertToDomain(JpaMeeting jpaMeeting) {
        return modelMapper.map(jpaMeeting, Meeting.class);
    }
}
