package com.stockanalyzer.processor;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;

import com.stockanalyzer.model.HistoricalData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YahooFinanceDataProcessor {
	@Value("${stock.symbols}")
	private String stockSymbols;
	@Value("${stock.range}")
	private String stockRange;
	
	@Value("${stock.interval}")
	private String stockInterval;

	private ConcurrentLinkedDeque<String> symbolsQueue;

	private ConcurrentLinkedDeque<HistoricalData> historicalDataQueue;

	private ScheduledExecutorService scheduler;

	public YahooFinanceDataProcessor() {
		symbolsQueue = new ConcurrentLinkedDeque<String>();
		historicalDataQueue = new ConcurrentLinkedDeque<HistoricalData>();
		scheduler = Executors.newScheduledThreadPool(2);
	}

	public void start() {
		log.info("Stock Processing Start");

		symbolsQueue = getStockSymblos();
		// Schedule the job to run every Month
		scheduler.scheduleAtFixedRate(new YahooFinanceDataRetrievalJob(this.symbolsQueue, this.historicalDataQueue, stockRange, stockInterval), 0,
				30, TimeUnit.DAYS);
		scheduler.scheduleAtFixedRate(new YahooFinanceDataPersisterJob(this.historicalDataQueue), 0, 30,
				TimeUnit.DAYS);

		log.info("Stock Processing End");
	}

	private ConcurrentLinkedDeque<String> getStockSymblos() {
		log.info("Stock Symbols {}", stockSymbols);
		if (stockSymbols != null) {
			String[] tokens = stockSymbols.split(":");

			for (String s : tokens) {
				symbolsQueue.add(s);
			}
		}
		return symbolsQueue;
	}
}