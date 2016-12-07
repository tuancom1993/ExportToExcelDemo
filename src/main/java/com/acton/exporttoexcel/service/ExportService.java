package com.acton.exporttoexcel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.jxls.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acton.exporttoexcel.entity.Report;
import com.acton.exporttoexcel.repository.ReportRepoInterface;

@Service
public class ExportService {
	@Autowired ReportRepoInterface reportRepoInterface;
	
	public File getReportFileByPOI(int idReport, String extension){
		try {
			Report report = reportRepoInterface.findOne(idReport);
			Workbook workbook = null;
			if ("xls".equals(extension)){
				workbook = new HSSFWorkbook();
			}
			else {
				workbook =new XSSFWorkbook();
			}
		
			Sheet sheet = workbook.createSheet("MySheet");
			
			CellStyle style_header = workbook.createCellStyle();
			Font font_header = workbook.createFont();
			font_header.setBold(true);
			font_header.setFontHeight((short)(36*20));
			font_header.setColor(IndexedColors.GREEN.index);
			style_header.setFont(font_header);
			
			sheet.createRow(0).createCell(0).setCellValue("Tite: "+report.getTitle());
			
			sheet.createRow(1).createCell(0).setCellValue(report.getHeader());
			sheet.getRow(1).getCell(0).setCellStyle(style_header);
			
			writeImageToExcel(report.getImageURL(), workbook, sheet, 4, 0, 10, 10);
			
			sheet.createRow(11).createCell(0).setCellValue("Footer:"+report.getFooter());
			
			sheet.createRow(13).createCell(0).setCellValue(3);
			sheet.createRow(14).createCell(0).setCellValue(13);
			sheet.createRow(15).createCell(0);
			sheet.getRow(15).getCell(0).setCellType(Cell.CELL_TYPE_FORMULA);
			sheet.getRow(15).getCell(0).setCellFormula("SUM(A14:A15)");
			
			File reportFile = new File("src/main/resources/static/excel/result/Report_"+report.getTitle()+"."+extension);
			
			sheet.getPrintSetup().setLandscape(true);
			sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); 
			
			FileOutputStream fileOutputStream = new FileOutputStream(reportFile);
			workbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			System.out.println("File done...!");
			return reportFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private void writeImageToExcel(String imageURL ,Workbook workbook, Sheet sheet ,int indexRow1, int indexCol1, int indexRow2, int indexCol2){
		try {
			InputStream inputStream = new FileInputStream(imageURL);
			byte[] imageBytes = IOUtils.toByteArray(inputStream);
			
			System.out.println(imageBytes.length);
			
			int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
			inputStream.close();
			CreationHelper helper = workbook.getCreationHelper();
			
			Drawing drawing = sheet.createDrawingPatriarch();
			
			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
			
			anchor.setRow1(indexRow1);
			anchor.setCol1(indexCol1);
			anchor.setRow2(indexRow2);
			anchor.setCol2(indexCol2);
			
			Picture picture = drawing.createPicture(anchor, pictureureIdx);
			picture.resize(1.0, 1.0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public File getReportFileByXLSX(int idReport, String type) {
		try {
			Report report = reportRepoInterface.findOne(idReport);
			ArrayList<Report> reports =  (ArrayList<Report>) reportRepoInterface.findAll();
			System.out.println("Size: "+reports.size());
			InputStream is = new FileInputStream("src/main/resources/static/excel/template/template-report.xlsx");
//			InputStream is = ExportService.class.getResourceAsStream("src/main/resources/static/excel/template/template-report.xlsx");

			File fileResult = new File("src/main/resources/static/excel/result/ReportByJXLS."+type);
			OutputStream os = new FileOutputStream(fileResult);
			Context context = new Context();
			
			byte[] bytesImage = new byte[0];
			try {
				InputStream is_impage = new FileInputStream(report.getImageURL());
				bytesImage = Util.toByteArray(is_impage);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("Chay den day");
			context.putVar("report", report);
			context.putVar("reports", reports);
			context.putVar("image", bytesImage);
			context.putVar("isEmpty", true);
			
			JxlsHelper.getInstance().processTemplate(is, os, context);
			
			return fileResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
