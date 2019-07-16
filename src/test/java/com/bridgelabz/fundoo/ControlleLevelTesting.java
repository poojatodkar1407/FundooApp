package com.bridgelabz.fundoo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;

import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.UserDTO;
import com.bridgelabz.fundoo.user.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ControlleLevelTesting {

	
	@Autowired
	private TestRestTemplate testRestTemplate;
	


	private String getRootUrl() {
		return "http://localhost:8080/user/register";
	}
	UserDTO user;
	LoginDTO login1;
	@Before
	public void setUser() {
		user = new UserDTO();
		login1 = new LoginDTO();
	}

	@Test
	public void userRegistrationAtController()
	{
	
		user.setFirstName("aarti");
		user.setLastName("todkar");
		user.setEmailId("brfewa1@gmail.com");
		user.setPassword("pooja124");
		user.setMobileNum("7304278325");
		System.out.println(user);
		//System.out.println("status code"+(testRestTemplate.postForEntity(getRootUrl(), user ,Response.class)).getBody().getStatusCode());
	 	
	assertEquals(200, (testRestTemplate.postForEntity(getRootUrl(), user,Response.class)).getBody().getStatusCode());
		
	}
	
	@Test
	public void userLoginController()
	{
		login1.setEmailId("poojatodkar124@gmail.com");
		login1.setPassword("Pooja@123");
		System.out.println("Model" +login1);
		//System.out.println((testRestTemplate.postForEntity("http://localhost:8080/user/login", login1, ResponseToken.class)).getBody());
		assertEquals(200, (testRestTemplate.postForEntity("http://localhost:8080/user/login", login1, ResponseToken.class)).getBody().getStatusCode());
	}

	

}
