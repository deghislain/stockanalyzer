package com.stockanalyzer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stockanalyzer.processor.YahooFinanceDataProcessor;

@Configuration
public class StockAnalyzerConfig {
	  @Bean
	    public YahooFinanceDataProcessor yahooFinanceDataProcessor() {
	        return new YahooFinanceDataProcessor();
	    }
}
