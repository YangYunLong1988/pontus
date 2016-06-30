package com.snowstore.pontus.service.vo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

public class DataTables<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6762942222301494342L;
	
	private Integer draw;
	
	private Long recordsTotal;
	
	private Long recordsFiltered;
	
	private List<T> data;
	
	private String error;

	public DataTables (Page<T> page){
		this.data = page.getContent();
		this.recordsTotal = page.getTotalElements();
		this.recordsFiltered = page.getTotalElements();
	}
	
	public DataTables (){
		
	}
	
	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
