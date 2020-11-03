package com.wannistudio.talk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class TalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkApplication.class, args);
	}

}
