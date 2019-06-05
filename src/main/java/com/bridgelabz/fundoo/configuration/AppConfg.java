package com.bridgelabz.fundoo.configuration;

import org.modelmapper.ModelMapper;


import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfg {
	/**
	 * Purpose : Creating bean object for PasswordEncoder
	 * @return : Return the bean object
	 */
	@Bean
	public PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder();
	}
	/**
	 * Purpose : Creating bean object for ModelMapper
	 * @return : Return the bean object
	 */

	@Bean
	public ModelMapper modelMapper() {
	    ModelMapper modelMapper = new ModelMapper();
	    modelMapper.getConfiguration()
	        .setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
	
}