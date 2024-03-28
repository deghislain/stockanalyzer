package com.stockanalyzer.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.stockanalyzer.model.HistoricalData;
import com.stockanalyzer.model.MonthlyInfo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StockAnalyzerDAOImpl implements StockAnalyzerDAO {
	public StockAnalyzerDAOImpl() {
		try {
			HikariConfig config = new HikariConfig();
			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String appConfigPath = rootPath + "application.properties";
			Properties appProps = new Properties();
			appProps.load(new FileInputStream(appConfigPath));
			config.setJdbcUrl(appProps.getProperty("jdbcUrl"));
			config.setUsername(appProps.getProperty("dbUsername"));
			config.setPassword(appProps.getProperty("dbPassword"));
			config.setDriverClassName(org.postgresql.Driver.class.getName());
			config.setMaximumPoolSize(10); // Adjust pool size as needed
			dataSource = new HikariDataSource(config);
		} catch (Exception e) {
			System.out.println("Error: Unable to open the application properties file");
		}
	}

	private HikariDataSource dataSource;

	@Override
	public void saveHistoricalData(HistoricalData hd) {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO Historical_data (historical_data_id,avg_close,avg_high,avg_low,avg_open,avg_volume,currency,historical_date,month_close,month_high,month_low,month_open,symbol) \r\n"
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			statement.setString(1, hd.getHistoricalDataId());
			statement.setDouble(2, hd.getAvgClose());
			statement.setDouble(3, hd.getAvgHigh());
			statement.setDouble(4, hd.getAvgLow());
			statement.setDouble(5, hd.getAvgOpen());
			statement.setDouble(6, hd.getAvgVolume());
			statement.setString(7, hd.getCurrency());
			statement.setDate(8, java.sql.Date.valueOf(hd.getHistoricalDate()));
			statement.setDouble(9, hd.getClose());
			statement.setDouble(10, hd.getHigh());
			statement.setDouble(11, hd.getLow());
			statement.setDouble(12, hd.getOpen());
			statement.setString(13, hd.getSymbol());

			int result = statement.executeUpdate();

			if (result == 1) {
				saveMonthlyInfo(connection, hd.getCurrentMonthInfoList());
			}

		} catch (SQLException e) {
			// Handle exceptions
			System.err.format("******************SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			log.error("Error: Unable to store user {}", e);
		}

	}

	private void saveMonthlyInfo(Connection connection, List<MonthlyInfo> currentMonthInfoList) {
		log.info("Saving saveMonthlyInfo Start");
		try {
		PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO monthly_info(monthly_info_id,current_month,month_close,month_date,month_high,monthl_low,month_open,month_volume,historical_data_id) \r\n"
						+ "VALUES (?,?,?,?,?,?,?,?,?)");
		for (MonthlyInfo mi : currentMonthInfoList) {
			log.info("Saving {}", mi);
			statement.setString(1, mi.getMonthlyInfoId());
			statement.setString(2, mi.getCurrentMonth());
			statement.setDouble(3, mi.getMonthClose());
			statement.setDate(4,new java.sql.Date(mi.getCurrentDate().getTime()));
			statement.setDouble(5, mi.getMonthHigh());
			statement.setDouble(6, mi.getMonthLow());
			statement.setDouble(7, mi.getMonthOpen());
			statement.setDouble(8, mi.getMonthVolume());
			statement.setString(9, mi.getHistoricalDataId());
			statement.executeUpdate();
		}
		}catch(Exception e) {
			log.error("error while saving monthly info {}",e);
		}
		log.info("Saving saveMonthlyInfo End");
	}

}
