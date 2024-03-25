package com.historical.data.processor;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.historical.data.model.HistoricalData;
import com.historical.data.model.MonthlyInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YahooFinanceDataRetrievalJob implements Runnable{
	private String stockRange;
	
	private String stockInterval;
	
	private ConcurrentLinkedDeque<String> symbolsQueue;
	
	private ConcurrentLinkedDeque<HistoricalData> historicalDataQueue;
	
	public YahooFinanceDataRetrievalJob(ConcurrentLinkedDeque<String> sq, ConcurrentLinkedDeque<HistoricalData> hdq, String sr, String si) {
		this.symbolsQueue = sq;
		this.historicalDataQueue = hdq;
		this.stockRange = sr;
		this.stockInterval = si;
	}

	@Override
	public void run() {
		log.info("Yahoo Finance Data Retrieval Job started");
		HistoricalData hd = getHistoricalData(symbolsQueue.getFirst(),stockRange, stockInterval);
		this.historicalDataQueue.add(hd);
		log.info("Yahoo Finance Data Retrieval Job Ended");
	}
	

	private static final String API_ENDPOINT = "https://query1.finance.yahoo.com/v8/finance/chart/%s?range=%s&interval=%s";
	private static HistoricalData historicalData = null;

	public HistoricalData getHistoricalData(String symbol, String range, String interval) {
		log.info("getHistoricalData currently retrieving {}", symbol);
		try {
			String urlString = String.format(API_ENDPOINT, symbol, range, interval);
			log.info("***************{}", urlString);
			URL url = new URL(urlString);
			byte[] bytes = new URL(urlString).openStream().readAllBytes();
			String jsonString = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
			JsonNode financialDataNode = getJsonNode(jsonString);
			financialDataNode.forEach(chartNode -> {
				JsonNode resultNode = chartNode.get("result");
				resultNode.forEach(rn -> {
					HistoricalData thData = new HistoricalData();
					JsonNode meta = rn.get("meta");
					if (meta.get("currency") != null && meta.get("currency").asText() != null) {
						thData.setCurrency(meta.get("currency").asText());
					}
					if (meta.get("symbol") != null && meta.get("symbol").asText() != null) {
						thData.setSymbol(meta.get("symbol").asText());
					}
					thData.setHistoricalDate(LocalDate.now());
					historicalData = getMonthlyInfo(rn, thData);
				});

			});
		} catch (Exception e) {

		}
		log.info("getHistoricalData end retrieving {}", historicalData);
		return historicalData;
	}

	private static JsonNode getJsonNode(String jsonString) {
		ObjectMapper obj = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = obj.readTree(jsonString);
		} catch (JsonMappingException e) {
			log.error("Error while mapping yahoo finance json file {}", e);
		} catch (JsonProcessingException e) {
			log.error("Error while Processing yahoo finance json file {}", e);
		}
		return jsonNode;
	}

	private static HistoricalData getMonthlyInfo(JsonNode resultNode, HistoricalData hd) {
		List<MonthlyInfo> mInfoList = new ArrayList<>();
		mInfoList = retrieveMonthlyDates(resultNode.get("timestamp"), mInfoList);
		hd.setCurrentMonthInfoList(mInfoList);
		JsonNode indicatorsNode = resultNode.get("indicators");
		JsonNode quotesNode = indicatorsNode.get("quote");
		quotesNode.forEach(q -> {
			retrieveMonthlyVolumes(q.get("volume"), hd);
			retrieveMonthlyHigh(q.get("high"), hd);
			retrieveMonthlyClose(q.get("close"), hd);
			retrieveMonthlyOpen(q.get("open"), hd);
			retrieveMonthlyLow(q.get("low"), hd);
		});

		hd.setCurrentMonthInfoList(mInfoList);

		return hd;
	}

	private static List<MonthlyInfo> retrieveMonthlyDates(JsonNode timestampNode, List<MonthlyInfo> months) {
		for (JsonNode node : timestampNode) {
			long unix_seconds = Long.parseLong(node.asText());
			Date date = new Date(unix_seconds * 1000L);
			MonthlyInfo mi = new MonthlyInfo();
			LocalDate lDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
			mi.setCurrentDate(date);
			mi.setCurrentMonth(lDate.getMonth().name());
			months.add(mi);
		}
		return months;
	}

	private static HistoricalData retrieveMonthlyVolumes(JsonNode volumeNode, HistoricalData hd) {
		List<MonthlyInfo> mInfoList = hd.getCurrentMonthInfoList();
		int size = mInfoList.size();
		int index = 0;
		double sum = 0;
		for (JsonNode node : volumeNode) {
			long volume = Long.parseLong(node.asText());
			sum += volume;
			mInfoList.get(index++).setMonthVolume(volume);
			if (index > size - 1) {
				break;
			}
		}
		hd.setAvgVolume(sum / index);
		hd.setCurrentMonthInfoList(mInfoList);
		return hd;
	}

	private static HistoricalData retrieveMonthlyHigh(JsonNode volumeNode, HistoricalData hd) {
		List<MonthlyInfo> mInfoList = hd.getCurrentMonthInfoList();
		int size = mInfoList.size();
		int index = 0;
		double sum = 0;
		for (JsonNode node : volumeNode) {
			double high = Double.parseDouble(node.asText());
			if (index == 0) {
				hd.setHigh(high);
			}
			sum += high;
			mInfoList.get(index++).setMonthHigh(high);
			if (index > size - 1) {
				break;
			}
		}
		hd.setAvgHigh(sum / index);
		hd.setCurrentMonthInfoList(mInfoList);
		return hd;
	}

	private static HistoricalData retrieveMonthlyClose(JsonNode closeNode, HistoricalData hd) {
		List<MonthlyInfo> mInfoList = hd.getCurrentMonthInfoList();
		int size = mInfoList.size();
		int index = 0;
		double sum = 0;
		for (JsonNode node : closeNode) {
			double close = Double.parseDouble(node.asText());
			if (index == 0) {
				hd.setClose(close);
			}
			sum += close;
			mInfoList.get(index++).setMonthClose(close);
			if (index > size - 1) {
				break;
			}
		}
		hd.setAvgClose(sum / index);
		hd.setCurrentMonthInfoList(mInfoList);
		return hd;
	}

	private static HistoricalData retrieveMonthlyOpen(JsonNode openNode, HistoricalData hd) {
		List<MonthlyInfo> mInfoList = hd.getCurrentMonthInfoList();
		int size = mInfoList.size();
		int index = 0;
		double sum = 0;
		for (JsonNode node : openNode) {
			double open = Double.parseDouble(node.asText());
			if (index == 0) {
				hd.setOpen(open);
			}
			sum += open;
			if (index == 0) {
				hd.setOpen(open);
			}
			mInfoList.get(index++).setMonthOpen(open);
			if (index > size - 1) {
				break;
			}
		}
		hd.setAvgOpen(sum / index);
		hd.setCurrentMonthInfoList(mInfoList);
		return hd;
	}

	private static HistoricalData retrieveMonthlyLow(JsonNode lowNode, HistoricalData hd) {
		List<MonthlyInfo> mInfoList = hd.getCurrentMonthInfoList();
		int size = mInfoList.size();
		int index = 0;
		double sum = 0;
		for (JsonNode node : lowNode) {
			double low = Double.parseDouble(node.asText());
			if (index == 0) {
				hd.setLow(low);
			}
			sum += low;
			mInfoList.get(index++).setMonthLow(low);
			if (index > size - 1) {
				break;
			}
		}
		hd.setAvgLow(sum / index);
		hd.setCurrentMonthInfoList(mInfoList);
		return hd;
	}

}
