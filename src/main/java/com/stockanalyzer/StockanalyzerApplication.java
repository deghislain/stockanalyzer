package com.stockanalyzer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.historical.data.processor.YahooFinanceDataProcessor;

@SpringBootApplication
public class StockanalyzerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(StockanalyzerApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			YahooFinanceDataProcessor yahooDatarocessor = ctx.getBean(YahooFinanceDataProcessor.class);
			yahooDatarocessor.start();
		};
	}
}
