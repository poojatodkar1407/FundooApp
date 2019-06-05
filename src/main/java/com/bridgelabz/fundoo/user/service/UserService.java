package com.bridgelabz.fundoo.user.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.UserDTO;
import com.bridgelabz.fundoo.user.model.User;

@Service
public interface UserService {

	Response onRegister(UserDTO userDto) throws UserException, UnsupportedEncodingException;

	ResponseToken onLogin(LoginDTO loginDto) throws UserException, UnsupportedEncodingException;

	ResponseToken authentication(Optional<User> user, String password);

	Response validateEmailId(String token);

	Response resetPaswords(String token, String password);

	Response forgetPassword(String emailId) throws UserException, UnsupportedEncodingException;

}
