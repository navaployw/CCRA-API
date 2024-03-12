package com.arg.ccra3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @Controller
@SpringBootApplication
public class ApiOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiOnlineApplication.class, args);
	}

	// private final String PATH = "/error";

    // @RequestMapping(value = PATH, method = RequestMethod.GET)
    // public String error() {
    //     return "forward:/index.html";
    // }

}
