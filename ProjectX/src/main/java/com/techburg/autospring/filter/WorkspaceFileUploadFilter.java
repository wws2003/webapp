package com.techburg.autospring.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import com.techburg.autospring.controller.ErrorController;

public class WorkspaceFileUploadFilter extends OncePerRequestFilter {

	private String[] mAcceptedFileTypes = null;
	private MultipartResolver mMultipartResolver = null;
	
	@Autowired
	public void setMultipartResolver(MultipartResolver multipartResolver) {
		mMultipartResolver = multipartResolver;
	}
	
	@Autowired
	public void setAcceptedFileTypes(String[] acceptedFileTypes) {
		mAcceptedFileTypes = acceptedFileTypes;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		MultipartHttpServletRequest multipartHttpServletRequest = mMultipartResolver.resolveMultipart(request);
		MultipartFile uploadFile = getUploadFileFromRequest(multipartHttpServletRequest);
		if(uploadFile != null) {
			try {
				validateUploadFile(uploadFile);
			} catch (RuntimeException e) {
				forwardToErrorPage(request, response, e.getMessage());
				return;
			}
		}
		filterChain.doFilter(multipartHttpServletRequest, response);
	}
	
	private MultipartFile getUploadFileFromRequest(MultipartHttpServletRequest request) {
		Map<String, MultipartFile> multipartMap = request.getFileMap();
		
		//Support only one file is uploaded
		if(multipartMap.isEmpty()) {
			return null;
		}
		else {
			for(Entry<String, MultipartFile> entry : multipartMap.entrySet()) {
				//Return immediately first file found in the map
				return entry.getValue();
			}
			return null;
		}
	}
	
	private void validateUploadFile(MultipartFile file) throws RuntimeException {
		if(file.isEmpty()) {
			//Allow no upload file
			return;
		}
		String fileType = getFileType(file);
		for(String acceptedFileType: mAcceptedFileTypes) {
			if(fileType != null && fileType.equals(acceptedFileType)) {
				return;
			}
		}
		throw new RuntimeException("Not supported upload file type");
	}
	
	private void forwardToErrorPage(HttpServletRequest request, 
			HttpServletResponse response, 
			String errorMessage) throws ServletException, IOException {
		
		//Using forward instead of redirect now
		/*RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/error");
		redirectView.setContextRelative(true);
		redirectView.setExposeModelAttributes(false);
		
		Properties errorProperties = new Properties();
		errorProperties.put("errorMessage", errorMessage);	
		redirectView.setAttributes(errorProperties);
	
		try {
			redirectView.render(null, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		request.setAttribute(ErrorController.ERROR_MESSAGE_ATTRIBUTE_NAME, errorMessage);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/error_page");
		dispatcher.forward(request, response);
	}
	
	private String getFileType(MultipartFile file) {
		//FIXME Currently just parse from file content type. More reliable method is desired
		return file.getContentType();
	}
	
}
