package com.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.test.entity.Tbblogpost;
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
import org.springframework.web.bind.annotation.*;

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

	@GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> ping(){

		String result = "Welcome to blog API";

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@PostMapping(path = "/saveBlogPost", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveBlogPost(@RequestBody Tbblogpost body){
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			Tbblogpost dataSave = mainService.saveBlogPost(body);
			result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataSave);
		} catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@GetMapping(path = "/getAllBlogPost", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getBlogPost(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize){
		ObjectMapper mapper = new ObjectMapper();
		String result = "";

		try {
			List<Tbblogpost> data = mainService.getAllBlogPost(pageNum, pageSize);
			result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		} catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@GetMapping(path = "/getAllBlogPost/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> findBlogPostById(@PathVariable("id") Integer id){
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			Optional<Tbblogpost> data = mainService.findBlogPostById(id);
			result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data.get());
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@PutMapping(path = "/updateBlogPost", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateBlogPost(@RequestBody Tbblogpost body){
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		Map<String, Object> response = new HashMap<>();

		try {
			response = mainService.updateBlogPost(body);
			if ((Integer) response.get("rspnCd") == 0){
				result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
				return new ResponseEntity<String>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.get("data"));
			}
		} catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@DeleteMapping(path = "/deleteById")
	public ResponseEntity<String> deleteBlogPostById(@RequestParam("id") Integer id){

		try {
			mainService.deleteById(id);
		} catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>("delete user successfully",HttpStatus.OK);
	}

}









