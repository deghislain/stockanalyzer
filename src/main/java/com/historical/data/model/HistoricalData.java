package com.historical.data.model;

import java.time.LocalDate;
import java.util.List;

public class HistoricalData {
	 private LocalDate historicalDate;
     private double open;
     private double avgOpen;
     private double high;
     private double avgHigh;
     private double low;
     private double avgLow;
     private double close;
     private double avgClose;
     private double avgVolume;
     private String currency;
     private String symbol;
     private List<MonthlyInfo> currentMonthInfoList;
	
     
	public LocalDate getHistoricalDate() {
		return historicalDate;
	}
	public void setHistoricalDate(LocalDate historicalDate) {
		this.historicalDate = historicalDate;
	}
	public double getAvgOpen() {
		return avgOpen;
	}
	public void setAvgOpen(double avgOpen) {
		this.avgOpen = avgOpen;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getAvgHigh() {
		return avgHigh;
	}
	public void setAvgHigh(double avgHigh) {
		this.avgHigh = avgHigh;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getAvgLow() {
		return avgLow;
	}
	public void setAvgLow(double avgLow) {
		this.avgLow = avgLow;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public double getAvgClose() {
		return avgClose;
	}
	public void setAvgClose(double avgClose) {
		this.avgClose = avgClose;
	}
	public double getAvgVolume() {
		return avgVolume;
	}
	public void setAvgVolume(double avgVolume) {
		this.avgVolume = avgVolume;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public List<MonthlyInfo> getCurrentMonthInfoList() {
		return currentMonthInfoList;
	}
	public void setCurrentMonthInfoList(List<MonthlyInfo> currentMonthInfoList) {
		this.currentMonthInfoList = currentMonthInfoList;
	}
	
	 @Override
	    public String toString() {
	        return "HistoricalData{" +
	                "symbol='" + symbol + '\'' +
	                ", historicalDate='" + historicalDate + '\'' +
	                ", open=" + open +
	                ", close=" + close +
	                '}';
	    }
}
