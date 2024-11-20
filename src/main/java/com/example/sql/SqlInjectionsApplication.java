package com.example.sql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SqlInjectionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlInjectionsApplication.class, args);
	}

}
