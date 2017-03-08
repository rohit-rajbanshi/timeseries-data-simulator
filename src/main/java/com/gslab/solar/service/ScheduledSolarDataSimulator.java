package com.gslab.solar.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ge.predix.solsvc.ext.util.JsonMapper;
import com.ge.predix.solsvc.restclient.impl.RestClient;
import com.gslab.solar.domain.SolarPanel;
import com.gslab.solar.rest.SimulatorController;

@Service
public class ScheduledSolarDataSimulator {

	@Autowired
	JsonMapper jsonMapper;

	@Autowired
	RestClient restClient;

	@Value("${gslab.timeseries.ingestUrl}")
	private String ingestUrl;

	public void postDatatoTimeSeries(SolarPanel panel) {

		String request = "[" + this.jsonMapper.toJson(panel) + "]";
		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Content-Type", "application/json"));
		// SimulatorController.LOG.info(ingestUrl);
		CloseableHttpResponse response = restClient.post(ingestUrl, request, headers);
		SimulatorController.LOG.info(request);
		SimulatorController.LOG.info(response.toString());
		EntityUtils.consumeQuietly(response.getEntity());
	}

}
