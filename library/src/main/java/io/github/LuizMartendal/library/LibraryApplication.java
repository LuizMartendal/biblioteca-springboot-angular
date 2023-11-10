package io.github.LuizMartendal.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		//System.out.println("hash " + new BCryptPasswordEncoder().encode("1234"));
		SpringApplication.run(LibraryApplication.class, args);
	}

}
