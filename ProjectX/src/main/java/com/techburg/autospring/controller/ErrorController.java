package com.techburg.autospring.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//MARK: Possibly only a temporary solution for error handler before move to more sophisticated solution

@Controller
public class ErrorController {

	public static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
	
	@RequestMapping(value="/error_page")
	public String toNewGithubWorkspacePage(Model model, 
			HttpServletRequest request) {
		
		String forwardedErrorMessage = (String) request.getAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME);
		if(forwardedErrorMessage != null) {
			model.addAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, forwardedErrorMessage);
		}
		return "error";
	}
}
