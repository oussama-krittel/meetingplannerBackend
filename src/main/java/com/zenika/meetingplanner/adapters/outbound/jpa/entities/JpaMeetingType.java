package com.zenika.meetingplanner.adapters.outbound.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "meeting_types")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JpaMeetingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int minimumCapacity;

    @ManyToMany
    @JoinTable(
            name = "meeting_type_equipment",
            joinColumns = @JoinColumn(name = "meeting_type_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private List<JpaEquipment> requiredEquipment;

}

