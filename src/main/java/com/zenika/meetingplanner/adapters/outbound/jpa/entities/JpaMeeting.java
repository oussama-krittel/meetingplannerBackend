package com.zenika.meetingplanner.adapters.outbound.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "meetings")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JpaMeeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int hour;

    @Column(nullable = false)
    private int participantCount;

    @ManyToOne
    @JoinColumn(name = "meeting_type_id", nullable = false)
    private JpaMeetingType type;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private JpaRoom room;

}
