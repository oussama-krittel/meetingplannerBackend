package com.zenika.meetingplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.zenika.meetingplanner")
public class MeetingplannerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MeetingplannerApplication.class, args);
	}

}
