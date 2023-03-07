package com.ece.sfs;

import com.ece.sfs.io.IOUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SFSApplication {

	public static void main(String[] args) {
		System.setProperty("org.jline.terminal.dumb", "true");
		IOUtil.init().bimap(
				v -> {
					SpringApplication.run(SFSApplication.class, args);

					return null;
				},
				s -> {
					System.err.println(s);

					return null;
				}
		);
	}

}
