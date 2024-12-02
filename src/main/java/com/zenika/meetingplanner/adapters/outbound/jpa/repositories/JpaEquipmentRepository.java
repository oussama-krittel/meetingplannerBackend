package com.zenika.meetingplanner.adapters.outbound.jpa.repositories;

import com.zenika.meetingplanner.adapters.outbound.jpa.entities.JpaEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEquipmentRepository extends JpaRepository<JpaEquipment, Long> {
}
