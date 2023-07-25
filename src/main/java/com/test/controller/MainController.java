package com.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.entity.TbUser;
import com.test.service.MainService;

@RestController
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	MainService mainService;
	
	String uriGetJobList = "http://dev3.dansmultipro.co.id/api/recruitment/positions.json";
	String uriGetJobDetail = "http://dev3.dansmultipro.co.id/api/recruitment/positions/";
	
	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login(@RequestBody String body) throws JsonMappingException, JsonProcessingException {
		String response = "";
		ObjectMapper mapper = new ObjectMapper();
		TbUser userRequest = mapper.readValue(body, TbUser.class);
		
		try {
			userRequest = mainService.login(userRequest);
			response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userRequest);
			if("0".equalsIgnoreCase(userRequest.getResponseCode())) {
				return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	@GetMapping(path = "/getJobList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getJobList(){
		
		logger.info("uri get job list hitted start");
		String result = httpRequest(uriGetJobList);
		logger.info("uri get job list hitted end");
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@GetMapping(path = "/getJobDetail/{ID}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getJobDetail(@PathVariable("ID") String id){
		
		logger.info("uri get job detail hitted start");
		String result = httpRequest(uriGetJobDetail+id);
		logger.info("uri get job detail hitted end");
		
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	public String httpRequest(String uri) {
		Map<String, Object> map = new HashMap<String, Object>();
		String jsonResult = "";
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(uri);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				String reasonPhrase = response.getStatusLine().getReasonPhrase();
				if (statusCode != 200) {
					map.put("code", String.valueOf(statusCode));
					map.put("result", false);
					map.put("resultDesc", reasonPhrase);
					jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
				} else {
					jsonResult = EntityUtils.toString(response.getEntity());
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return jsonResult;
	}

}









