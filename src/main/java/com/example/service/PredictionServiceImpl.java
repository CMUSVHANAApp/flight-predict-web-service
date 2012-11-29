package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PredictionServiceImpl implements PredictionService {

	@PersistenceContext
    EntityManager em;
	
	@Transactional
	public String test() {
		return "Hello";
	}

}
