package com.techburg.autospring.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HelloController {
	@RequestMapping(value="/home", method=RequestMethod.GET) 
	public String hello() {
		return "hello";
	}
}

