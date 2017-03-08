package com.gslab.solar.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.gslab.solar.domain.SolarPanel;
import com.gslab.solar.service.ScheduledSolarDataSimulator;

@Component
@Controller
@Configuration
@EnableScheduling
public class SimulatorController {

	public static final Logger LOG = LoggerFactory.getLogger(SimulatorController.class);

	Double currentLow = 1.0, currentHigh = 4.0;
	Double voltageInLow = 27.0, voltageInHigh = 32.0;
	Double voltageOutLow = 27.0, voltageOutHigh = 32.0;
	Double temperaturelow = 27.0, temperaturehigh = 31.0;

	@Autowired
	ScheduledSolarDataSimulator scheduledSolarDataSimulator = new ScheduledSolarDataSimulator();

	@SuppressWarnings({ "nls", "deprecation" })
	@Scheduled(fixedRate = 60000)
	public void postData() {

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
//		int value = 19;
//		calendar.set(Calendar.HOUR_OF_DAY, value);
		int hrs = calendar.get(Calendar.HOUR_OF_DAY);

		if (hrs >= 8 && hrs <= 10) {
			voltageInLow = 30.0;
			voltageInHigh = 33.0;
			voltageOutLow = 31.0;
			voltageOutHigh = 33.0;
			currentLow = 1.0;
			currentHigh = 2.0;
			temperaturelow = 25.0;
			temperaturehigh = 28.0;
		} else if (hrs >= 11 && hrs <= 13) {
			voltageInLow = 32.0;
			voltageInHigh = 35.0;
			voltageOutLow = 32.0;
			voltageOutHigh = 36.0;
			currentLow = 1.5;
			currentHigh = 3.0;
			temperaturelow = 28.0;
			temperaturehigh = 36.0;
		} else if (hrs >= 14 && hrs <= 16) {
			voltageInLow = 33.0;
			voltageInHigh = 36.0;
			voltageOutLow = 35.0;
			voltageOutHigh = 38.0;
			currentLow = 1.0;
			currentHigh = 4.0;
			temperaturelow = 34.0;
			temperaturehigh = 37.0;
		} else if (hrs >= 17 && hrs < 18) {
			voltageInLow = 27.0;
			voltageInHigh = 32.0;
			voltageOutLow = 27.0;
			voltageOutHigh = 32.0;
			currentLow = 0.5;
			currentHigh = 1.5;
			temperaturelow = 27.0;
			temperaturehigh = 31.0;
		} else {
			if (voltageInLow <= 3)
				voltageInLow = 0.0;
			else
				voltageInLow -= 3.0;

			if (voltageInHigh <= 3)
				voltageInHigh = 0.0;
			else
				voltageInHigh -= 3.0;

			if (voltageOutLow <= 3)
				voltageOutLow = 0.0;
			else
				voltageOutLow -= 3.0;

			if (voltageOutHigh <= 3)
				voltageOutHigh = 0.0;
			else
				voltageOutHigh -= 3.0;

			if (currentLow <= 0.2)
				currentLow = 0.0;
			else
				currentLow -= 0.2;

			if (currentHigh <= 0.2)
				currentHigh = 0.0;
			else
				currentHigh -= 0.2;

			if (temperaturelow <= 14)
				temperaturelow = 14.0;
			else
				temperaturelow -= 0.1;

			if (temperaturehigh <= 20)
				temperaturehigh = 20.0;
			else
				temperaturehigh -= 0.1;
		}

		List<SolarPanel> panels = new ArrayList<SolarPanel>();
		for (Integer i = 4; i < 9; i++) {
			SolarPanel panel = new SolarPanel();
			panel.setPanelId("10" + i.toString());
			panel.setCurrent(generateRandomUsageValue(currentLow, currentHigh));
			panel.setTemperature(generateRandomUsageValue(temperaturelow, temperaturehigh));
			panel.setVoltageIn(generateRandomUsageValue(voltageInLow, voltageInHigh));
			panel.setVoltageOut(generateRandomUsageValue(voltageOutLow, voltageOutHigh));
			System.out.println(panel.getPanelId() + "\t" + panel.getVoltageIn() + "\t" + panel.getCurrent() + "\t" +	 panel.getVoltageOut() + "\t" + panel.getTemperature());
			panels.add(panel);
		}

		 for (SolarPanel p : panels)
		 scheduledSolarDataSimulator.postDatatoTimeSeries(p);
		 LOG.info("Data posted to timeseries");
	}

	private static double generateRandomUsageValue(double low, double high) {
		return low + Math.random() * (high - low);
	}

}
