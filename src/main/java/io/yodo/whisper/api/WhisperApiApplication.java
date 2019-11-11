package io.yodo.whisper.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class WhisperApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhisperApiApplication.class, args);
	}

}
