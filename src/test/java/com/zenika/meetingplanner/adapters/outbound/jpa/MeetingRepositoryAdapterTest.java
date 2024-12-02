package com.zenika.meetingplanner.adapters.outbound.jpa;

import com.zenika.meetingplanner.adapters.outbound.jpa.entities.JpaMeeting;
import com.zenika.meetingplanner.adapters.outbound.jpa.repositories.JpaMeetingRepository;
import com.zenika.meetingplanner.domain.Meeting;
import com.zenika.meetingplanner.domain.MeetingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingRepositoryAdapterTest {

    @Mock
    private JpaMeetingRepository jpaMeetingRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MeetingRepositoryAdapter meetingRepositoryAdapter;

    private Meeting domainMeeting;
    private JpaMeeting jpaMeeting;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize domain Meeting
        domainMeeting = Meeting.builder()
                .type(MeetingType.builder()
                        .name("SPEC")
                        .minimumCapacity(5)
                        .build())
                .participantCount(10)
                .date(LocalDate.of(2024, 12, 1))
                .hour(10)
                .build();

        // Initialize JPA Meeting
        jpaMeeting = JpaMeeting.builder()
                .id(1L)
                .date(LocalDate.of(2024, 12, 1))
                .hour(10)
                .participantCount(10)
                .build();
    }

    @Test
    void testSave_ShouldSaveMeetingSuccessfully() {
        // Arrange
        when(modelMapper.map(domainMeeting, JpaMeeting.class)).thenReturn(jpaMeeting);
        when(jpaMeetingRepository.save(jpaMeeting)).thenReturn(jpaMeeting);
        when(modelMapper.map(jpaMeeting, Meeting.class)).thenReturn(domainMeeting);

        // Act
        Meeting savedMeeting = meetingRepositoryAdapter.save(domainMeeting);

        // Assert
        assertNotNull(savedMeeting);
        assertEquals(domainMeeting.getDate(), savedMeeting.getDate());
        assertEquals(domainMeeting.getHour(), savedMeeting.getHour());
        assertEquals(domainMeeting.getParticipantCount(), savedMeeting.getParticipantCount());

    }

    @Test
    void testSave_ShouldMapDomainToEntityAndEntityToDomainCorrectly() {
        // Arrange
        when(modelMapper.map(domainMeeting, JpaMeeting.class)).thenReturn(jpaMeeting);
        when(jpaMeetingRepository.save(jpaMeeting)).thenReturn(jpaMeeting);
        when(modelMapper.map(jpaMeeting, Meeting.class)).thenReturn(domainMeeting);

        // Act
        meetingRepositoryAdapter.save(domainMeeting);

        // Capture arguments passed to save
        ArgumentCaptor<JpaMeeting> captor = ArgumentCaptor.forClass(JpaMeeting.class);
        verify(jpaMeetingRepository).save(captor.capture());
        JpaMeeting capturedJpaMeeting = captor.getValue();

        // Assert mappings
        assertEquals(jpaMeeting.getDate(), capturedJpaMeeting.getDate());
        assertEquals(jpaMeeting.getHour(), capturedJpaMeeting.getHour());
        assertEquals(jpaMeeting.getParticipantCount(), capturedJpaMeeting.getParticipantCount());
    }

}
