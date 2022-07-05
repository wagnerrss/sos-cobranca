package com.fw.cobranca;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SOSCobrancaApplication extends SpringBootServletInitializer implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SOSCobrancaApplication.class, args);
		System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
		System.out.println("jdk.http.auth.tunneling.disabledSchemes");
		System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
		System.out.println("jdk.http.auth.proxying.disabledSchemes");
	}
	@Override
	public void run(String... args) throws Exception {
	}
}
