package com.snowstore.pontus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.snowstore.pontus.service.MemberAttachmentService;

@Controller
@RequestMapping("/attach")
public class AttachmentController {
	@Autowired
	private MemberAttachmentService memberAttachmentService;

	@ResponseBody
	@RequestMapping("/upload")
	public WebResult upload(@RequestParam CommonsMultipartFile file) {
		try {
			memberAttachmentService.save(file.getBytes(), file.getOriginalFilename(), file.getContentType());
			return WebResult.newExceptionWebResult();
		} catch (Exception e) {
			return WebResult.newExceptionWebResult();
		}
	}
}
