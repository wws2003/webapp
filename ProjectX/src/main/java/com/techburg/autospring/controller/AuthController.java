package com.techburg.autospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController {

	/**
	 * Logon controller
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/logon", method = RequestMethod.GET)
	public String logon(Model model) {
		//For test first
		return "logon";
	}
}
