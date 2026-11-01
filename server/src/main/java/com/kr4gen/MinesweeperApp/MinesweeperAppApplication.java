package com.kr4gen.MinesweeperApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MinesweeperAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinesweeperAppApplication.class, args);
	}

}
