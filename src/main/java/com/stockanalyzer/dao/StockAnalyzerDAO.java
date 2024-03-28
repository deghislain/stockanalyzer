package com.stockanalyzer.dao;

import com.stockanalyzer.model.HistoricalData;

public interface StockAnalyzerDAO {
	public void saveHistoricalData(HistoricalData hd);
}
