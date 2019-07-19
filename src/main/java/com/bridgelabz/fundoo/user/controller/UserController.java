package com.bridgelabz.fundoo.user.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.note.service.NoteService;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.EmailDto;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.PasswordDto;
import com.bridgelabz.fundoo.user.dto.UserDTO;
import com.bridgelabz.fundoo.user.service.UserService;

@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private NoteService noteService;

	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody UserDTO userDto)
			throws UserException, UnsupportedEncodingException {
System.out.println("in user controller"+userDto);
		Response response = userService.onRegister(userDto);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/login")
	public ResponseEntity<ResponseToken> onLogin(@RequestBody LoginDTO loginDTO)
			throws UserException, UnsupportedEncodingException {
		
		System.out.println("in login controller");
		ResponseToken response = userService.onLogin(loginDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/{token}/valid")
	public ResponseEntity<Response> emailValidation(@PathVariable String token) throws UserException {
		
		Response response = userService.validateEmailId(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestBody EmailDto emailDto)
			throws UnsupportedEncodingException, UserException, MessagingException {
		
		Response status = userService.forgetPassword(emailDto);
		return new ResponseEntity<Response>(status, HttpStatus.OK);

	}

	@PutMapping(value = "/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@PathVariable String token, @RequestBody PasswordDto passwordDto)
			throws UserException {
		Response response = userService.resetPasswords(token, passwordDto);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
	
	@PostMapping("/uploadImage")
	public ResponseEntity<Response> uploadImage(@RequestHeader String token , @RequestParam MultipartFile imageFile) throws IOException
	{
		Response response = userService.uploadImage(token, imageFile);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
		
	}
	
	@GetMapping("/getuploadedimage/{token}")
	public ResponseEntity<Resource> getProfilePic(@PathVariable String token) {
		Resource resourseStatus = userService.getUploadedImageOfUser(token);
		return new ResponseEntity<Resource> (resourseStatus, HttpStatus.OK);
}
}