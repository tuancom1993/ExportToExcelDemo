package com.acton.exporttoexcel.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.acton.exporttoexcel.entity.Report;
import com.acton.exporttoexcel.service.ReportService;

@Controller
public class HomeController {
	@Autowired ReportService reportService;
	
	@RequestMapping(value={"/", "/home"})
	public String showHomePage(Model model){
		ArrayList<Report> reports = reportService.getReports();
		System.out.println("Reposts size:"+reports.size());
		model.addAttribute("reports", reports);
		model.addAttribute("report", new Report());
		
		return "home";
	}
	
	@RequestMapping(value="/create-report", method = RequestMethod.POST)
	public String createReport(@ModelAttribute("report") Report report,
				@RequestParam("image") MultipartFile multipartFile){
		try {
			System.out.println(report.getFooter());
			reportService.createReport(report, multipartFile);
		} catch (Exception e) {
			return "redirect:/home?error=true";
		}
		return "redirect:/home";
	}
}
