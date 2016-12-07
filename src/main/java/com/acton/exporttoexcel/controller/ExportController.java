package com.acton.exporttoexcel.controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.acton.exporttoexcel.service.ExportService;

@Controller
public class ExportController {
	@Autowired ExportService exportService;
	
	@RequestMapping(value="/export")
	public void exportFile(@RequestParam("id") int idReport, 
			@RequestParam("type") String type, 
			@RequestParam("by") String by,
			HttpServletResponse response) throws IOException{
		if("poi".equals(by))
			IOUtils.copy(new FileInputStream(exportService.getReportFileByPOI(idReport, type)), response.getOutputStream());
		else if ("jxls".equals(by))
			IOUtils.copy(new FileInputStream(exportService.getReportFileByXLSX(idReport, type)), response.getOutputStream());
		response.flushBuffer();
	}
}
