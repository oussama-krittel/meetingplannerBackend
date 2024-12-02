package com.zenika.meetingplanner.adapters.outbound.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "rooms")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JpaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<JpaMeeting> reservations;

    @ManyToMany
    @JoinTable(
            name = "room_equipment",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private List<JpaEquipment> equipments;

}
