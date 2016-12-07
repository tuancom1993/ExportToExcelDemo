package com.acton.exporttoexcel.repository;

import org.springframework.data.repository.CrudRepository;

import com.acton.exporttoexcel.entity.Report;

public interface ReportRepoInterface extends CrudRepository<Report, Integer> {

}
