package com.zenika.meetingplanner.adapters.outbound.jpa.repositories;

import com.zenika.meetingplanner.adapters.outbound.jpa.entities.JpaMeetingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaMeetingTypeRepository extends JpaRepository<JpaMeetingType, Long> {
    Optional<JpaMeetingType> findByName(String meetingTypeName);

    boolean existsByName(String name);
}
