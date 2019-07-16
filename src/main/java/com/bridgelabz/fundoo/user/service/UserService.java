package com.bridgelabz.fundoo.user.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.EmailDto;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.PasswordDto;
import com.bridgelabz.fundoo.user.dto.UserDTO;
import com.bridgelabz.fundoo.user.model.User;

@Service
public interface UserService {

	Response onRegister(UserDTO userDto) throws UserException, UnsupportedEncodingException;

	ResponseToken onLogin(LoginDTO loginDto) throws UserException, UnsupportedEncodingException;

	ResponseToken authentication(Optional<User> user, String password);

	Response validateEmailId(String token);

	Response resetPasswords(String token, PasswordDto passwordDto);

	Response forgetPassword(EmailDto emailDto) throws UserException, UnsupportedEncodingException;

	Response uploadImage(String token, MultipartFile imageFile) throws IOException;

	Resource getUploadedImageOfUser(String token);

	

	

}
