package com.ece.sfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SFSApplication {

	private final Directory root;

	@Autowired
	public SFSApplication(Directory root) {
		this.root = root;
	}

	private void loop() {

	}

	public static void main(String[] args) {
		SpringApplication.run(SFSApplication.class, args);
	}

}
