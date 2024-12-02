package com.zenika.meetingplanner.common.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class RoomWithAvailableHoursDto {
    private String name;
    private int capacity;
    private List<String> roomEquipments;
    private List<String> availableHours;
}
