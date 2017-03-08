package com.gslab.solar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath*:META-INF/spring/predix-rest-client-scan-context.xml", "classpath*:META-INF/spring/ext-util-scan-context.xml" })
public class TimeseriesDataSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeseriesDataSimulatorApplication.class, args);

	}
}
