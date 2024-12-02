package com.zenika.meetingplanner.adapters.outbound.jpa;

import com.zenika.meetingplanner.adapters.outbound.jpa.entities.JpaMeetingType;
import com.zenika.meetingplanner.adapters.outbound.jpa.repositories.JpaMeetingTypeRepository;
import com.zenika.meetingplanner.domain.MeetingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingTypeRepositoryAdapterTest {

    @Mock
    private JpaMeetingTypeRepository jpaMeetingTypeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MeetingTypeRepositoryAdapter meetingTypeRepositoryAdapter;

    private JpaMeetingType jpaMeetingType;
    private MeetingType meetingType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        jpaMeetingType = new JpaMeetingType();
        jpaMeetingType.setName("SPEC");
        jpaMeetingType.setMinimumCapacity(5);

        meetingType = new MeetingType("SPEC", 5, new ArrayList<>());
    }

    @Test
    void testFindByName_ShouldReturnMeetingType_WhenMeetingTypeFound() {
        // Arrange
        when(jpaMeetingTypeRepository.findByName("SPEC")).thenReturn(Optional.of(jpaMeetingType));
        when(modelMapper.map(jpaMeetingType, MeetingType.class)).thenReturn(meetingType);

        // Act
        Optional<MeetingType> result = meetingTypeRepositoryAdapter.findByName("SPEC");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("SPEC", result.get().getName());
        assertEquals(5, result.get().getMinimumCapacity());

        // Verify interactions
        verify(jpaMeetingTypeRepository, times(1)).findByName("SPEC");
        verify(modelMapper, times(1)).map(jpaMeetingType, MeetingType.class);
    }

    @Test
    void testFindByName_ShouldReturnEmpty_WhenMeetingTypeNotFound() {
        // Arrange
        when(jpaMeetingTypeRepository.findByName("SPEC")).thenReturn(Optional.empty());

        // Act
        Optional<MeetingType> result = meetingTypeRepositoryAdapter.findByName("SPEC");

        // Assert
        assertFalse(result.isPresent());

        // Verify interaction
        verify(jpaMeetingTypeRepository, times(1)).findByName("SPEC");
        verifyNoInteractions(modelMapper);
    }

}
