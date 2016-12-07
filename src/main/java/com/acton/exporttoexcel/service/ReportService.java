package com.acton.exporttoexcel.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.acton.exporttoexcel.entity.Report;
import com.acton.exporttoexcel.repository.ReportRepoInterface;

@Service
public class ReportService {
	@Autowired ReportRepoInterface reportRepoInterface;
	
	public ArrayList<Report> getReports(){
		return (ArrayList<Report>) reportRepoInterface.findAll();
	}
	
	public void createReport(Report report, MultipartFile multipartFile) throws Exception{
		report.setImageURL(executeUploadFile(multipartFile));
		reportRepoInterface.save(report);
	}
	
	private String executeUploadFile(MultipartFile multipartFile) throws Exception{
		try {
			BufferedOutputStream stream = null;
			String imageURL = "";
			File dest = new File("src/main/resources/static/img");
			if(!dest.exists()) dest.mkdirs();
			if(multipartFile.isEmpty()) throw new Exception("File is empty");
			else {
				String extensionFile = getExtensionFile(multipartFile.getOriginalFilename());
				String timeNow = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().getTime());
				String imageName = timeNow+"."+extensionFile;
				imageURL = dest+"/"+imageName;
				File image = new File(imageURL); 
						
				stream = new BufferedOutputStream(new FileOutputStream(image));
				FileCopyUtils.copy(multipartFile.getInputStream(), stream);
			}
			
			return imageURL;
		} catch (Exception e) {
			throw new Exception();
		}
	}

	private String getExtensionFile(String originalFileName) {
		String extension = "";
		String[] strs = originalFileName.split("\\.");
		if(strs != null) extension = strs[strs.length-1]; 
		return extension;
	}
}
