package com.goodfood.project.web;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;

@Controller
public class SearchController {

	@RequestMapping(value = "/search.do")
	public String main(ModelMap model) throws Exception {
//		BasicConfigurator.configure();

		String searchWord = "";
		String queryString = "";
		if (model.getAttribute("searchWord") != "") {
			searchWord = (String) model.getAttribute("searchWord");

			queryString = "{" + "\"query\": {" + "\"bool\": {" + "\"must\": [" +
//				"{\"match\":{\"gubun\":\""+param.get("gubun")+"\"}}," +
					"{\"match\":{\"local\":\"" + searchWord + "\"}}" + "]" + "}" + "}," + "\"size\": 10" + "}";
		} else {
			queryString = "{" + "\"query\": {" + "\"bool\": {" + "\"must\": [" +
						"{\"match\":{\"local\":\"아산\"}}" + "]" + "}" + "}," + "\"size\": 10" + "}";
		}
		System.out.println("----------충청남도 모범음식점------------- " + queryString);
		JestClient jestClient = jestClient("http://localhost:9200");
		JestResult jestResult;
		String jsonResult = "";

		try {
			jestResult = jestClient
					.execute(new Search.Builder(queryString).addIndex("chungnamfood").addType("search").build());
			jsonResult = jestResult.getJsonString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// jestClient.close();
		System.out.println(jsonResult);
		model.addAttribute("result", jsonResult);

		return "index2";
	}

	private static JestClient jestClient(String esUrl) {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(esUrl)
				// new HttpClientConfig.Builder("http://localhost:9200")
				.multiThreaded(true).build());
		return factory.getObject();
	}
}
