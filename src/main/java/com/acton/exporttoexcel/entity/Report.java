package com.acton.exporttoexcel.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the report database table.
 * 
 */
@Entity
@Table(name = "report")
@NamedQuery(name = "Report.findAll", query = "SELECT r FROM Report r")
public class Report implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "idreport")
	private int idReport;

	private String footer;

	private String header;

	@Column(name = "imageurl")
	private String imageURL;

	private String title;
	
	private int numberbug;

	public Report() {
	}

	public int getIdReport() {
		return idReport;
	}

	public void setIdReport(int idReport) {
		this.idReport = idReport;
	}

	public String getFooter() {
		return this.footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getHeader() {
		return this.header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNumberbug() {
		return numberbug;
	}

	public void setNumberbug(int numberbug) {
		this.numberbug = numberbug;
	}
	
	

}