package com.bridgelabz.fundoo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.bridgelabz.fundoo.exception.UserException;

import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.UserDTO;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FundooApp2ApplicationTests {

	@Mock
	private ModelMapper modelMapper;
	
	@Mock 
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private UserRepository userRepository;

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void userRegistrationTest()
	{
			UserDTO userdto = new UserDTO();
			User user = new User();
	
			when(modelMapper.map(userdto, User.class)).thenReturn(user);
			user.setFirstName("Pooja");
			user.setLastName("Todkar");
			user.setEmailId("poojatodkar124@gmail.com");
			user.setPassword("pooja@123");
			user.setMobileNum("123456789");
			when(passwordEncoder.encode(userdto.getPassword())).thenReturn(user.getPassword());
		//	when(userRepository.findByEmailId(userdto.getEmailId())).thenReturn(user);
			when(userRepository.save(user)).thenReturn(user);
			System.out.println(user);
			assertEquals("Pooja", user.getFirstName());
	}
	
	@Test
	public void userAuthentication() throws UserException, UnsupportedEncodingException
	{
		
		LoginDTO logindto = new LoginDTO();
		User user = new User();
		
		when(modelMapper.map(logindto,User.class)).thenReturn(user);
		
		logindto.setEmailId("poojatodkar124@gmail.com");
		logindto.setPassword("pooja@123");
		System.out.println(logindto);
		when(passwordEncoder.matches("pooja@123", logindto.getPassword())).thenReturn(true);
		assertEquals("poojatodkar124@gmail.com", logindto.getEmailId());	
	}

}


