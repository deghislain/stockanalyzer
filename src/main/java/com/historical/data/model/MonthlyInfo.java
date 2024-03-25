package com.historical.data.model;

import java.util.Date;

public class MonthlyInfo {
	private String currentMonth;
	 private Date currentDate;
     private double monthOpen;
     private double monthHigh;
     private double monthLow;
     private double monthClose;
     private long monthVolume;
     
     
	public String getCurrentMonth() {
		return currentMonth;
	}
	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}
	
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public double getMonthOpen() {
		return monthOpen;
	}
	public void setMonthOpen(double monthOpen) {
		this.monthOpen = monthOpen;
	}
	public double getMonthHigh() {
		return monthHigh;
	}
	public void setMonthHigh(double monthHigh) {
		this.monthHigh = monthHigh;
	}
	public double getMonthLow() {
		return monthLow;
	}
	public void setMonthLow(double monthLow) {
		this.monthLow = monthLow;
	}
	public double getMonthClose() {
		return monthClose;
	}
	public void setMonthClose(double monthClose) {
		this.monthClose = monthClose;
	}
	public long getMonthVolume() {
		return monthVolume;
	}
	public void setMonthVolume(long monthVolume) {
		this.monthVolume = monthVolume;
	}
	
}
