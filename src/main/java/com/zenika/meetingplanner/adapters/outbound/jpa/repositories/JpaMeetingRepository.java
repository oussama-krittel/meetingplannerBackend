package com.zenika.meetingplanner.adapters.outbound.jpa.repositories;

import com.zenika.meetingplanner.adapters.outbound.jpa.entities.JpaMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMeetingRepository extends JpaRepository<JpaMeeting, Long> {
}
