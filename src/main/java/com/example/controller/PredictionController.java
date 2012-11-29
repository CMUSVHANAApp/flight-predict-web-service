package com.example.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.Person;
import com.example.model.prediction.GooglePredictionWrapper;
import com.example.service.PersonService;
import com.example.service.PredictionService;
import java.util.Map;

@Controller
@RequestMapping("/prediction")
public class PredictionController {
	
	@Autowired
    private PredictionService predictionService;
	
		@RequestMapping
	    public String test(Map<String, Object> map) {

	        map.put("person", new GooglePredictionWrapper());
	        map.put("peopleList", predictionService.test());

	        return "prediction";
	    }
}
