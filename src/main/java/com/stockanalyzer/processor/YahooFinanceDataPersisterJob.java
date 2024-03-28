package com.stockanalyzer.processor;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.stockanalyzer.dao.*;
import com.stockanalyzer.model.HistoricalData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YahooFinanceDataPersisterJob implements Runnable{

	private ConcurrentLinkedDeque<HistoricalData> historicalDataQueue;

	public YahooFinanceDataPersisterJob(ConcurrentLinkedDeque<HistoricalData> hdq) {
		this.historicalDataQueue = hdq;
	}
	
	@Override
	public void run() {
		log.info("Persisisting Job Started");
		try {
			Thread.sleep(15000);
			while (this.historicalDataQueue.size() > 0) {
				log.info("historicalDataQueue {}", historicalDataQueue.size());
				saveHistoricalData(historicalDataQueue.pollFirst());
			}
			log.info("Persisisting Job Ended");
		} catch (InterruptedException e) {
			log.error("Error while persisting stock data {}", e);
		}

	}
	
	private void saveHistoricalData(HistoricalData hd) {
		log.info(" saveHistoricalData Start {}", hd);
	
		StockAnalyzerDAOImpl dao = new StockAnalyzerDAOImpl();
		dao.saveHistoricalData(hd);
		
		log.info(" saveHistoricalData End {}");
	}
}
