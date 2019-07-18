package com.bridgelabz.fundoo.user.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.EmailDto;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.PasswordDto;
import com.bridgelabz.fundoo.user.dto.UserDTO;
import com.bridgelabz.fundoo.user.model.EmailId;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepository;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenUtil;
import com.bridgelabz.fundoo.utility.Utility;

@PropertySource("classpath:message.properties")
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
public class UserServiceImpl implements UserService {

	@Autowired
	private RabbitMqProvider rabbitMqProvider;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private Response statusResponse;

	@Autowired
	private Environment environment;

	private final Path fileLocation = Paths.get("/home/admin1/Pictures/Wallpapers/");

	@Override
	public Response onRegister(UserDTO userDto) {
		EmailId emailId = new EmailId();
		String emailid = userDto.getEmailId();

		User user = modelMapper.map(userDto, User.class);
		Optional<User> useralreadyPresent = userRepo.findByEmailId(user.getEmailId());
		if (useralreadyPresent.isPresent()) {
			throw new UserException(environment.getProperty("status.register.emailExistError"));
		}
		// encode user password
		String password = passwordEncoder.encode(userDto.getPassword());
		System.out.println("HERE IM");
		user.setPassword(password);
		userRepo.save(user);
		Long userId = user.getUserId();
//		Utility.send(emailid, "confirmation mail", Utility.getUrl(userId) + "/valid");
		String url = Utility.getUrl(userId)+"/valid";
		emailId.setTo(emailid);
		emailId.setSubject("confirmation mail for registration");
		emailId.setBody(url);
		rabbitMqProvider.sendMessageToQueue(emailId);
		rabbitMqProvider.send(emailId);
		statusResponse = ResponseHelper.statusResponse(200, "register successfully");
		return statusResponse;

	}

	@Override
	public ResponseToken onLogin(LoginDTO loginDto) throws UserException, UnsupportedEncodingException {
		Optional<User> user = userRepo.findByEmailId(loginDto.getEmailId());
		ResponseToken response = new ResponseToken();
		if (user.isPresent()) {
			System.out.println(loginDto.getPassword());
			

			return authentication(user, loginDto.getPassword());

		}
		System.out.println(response);
		return response;

	}

	@Override
	public ResponseToken authentication(Optional<User> user, String password) {

		ResponseToken response = new ResponseToken();
		if (user.get().isVerify()) {
			boolean status = passwordEncoder.matches(password, user.get().getPassword());

			if (status == true) {
				String token = tokenUtil.createToken(user.get().getUserId());
				response.setToken(token);
				response.setStatusCode(200);
				response.setStatusMessage(environment.getProperty("user.login"));
				return response;
			}

			throw new UserException(401, environment.getProperty("user.login.password"));
		}

		throw new UserException(401, environment.getProperty("user.login.verification"));
	}

	@Override
	public Response validateEmailId(String token) {
		Long id = tokenUtil.decodeToken(token);
		User user = userRepo.findById(id)
				.orElseThrow(() -> new UserException(404, environment.getProperty("user.validation.email")));
		user.setVerify(true);
		userRepo.save(user);
		statusResponse = ResponseHelper.statusResponse(200, environment.getProperty("user.validation"));
		return statusResponse;
	}

	@Override
	public Response forgetPassword(EmailDto emailDto) throws UserException, UnsupportedEncodingException {
		Optional<User> useralreadyPresent = userRepo.findByEmailId(emailDto.getEmailId());
		if (!useralreadyPresent.isPresent()) {

			throw new UserException(401, environment.getProperty("user.forgetpassword.emaiId"));
		}
		
		Long id = useralreadyPresent.get().getUserId();
		Utility.send(emailDto.getEmailId(), "password reset mail", Utility.getUrl1(id));
		return ResponseHelper.statusResponse(200, environment.getProperty("user.forgetpassword.link"));
	}

	@Override
	public Response resetPasswords(String token, PasswordDto passwordDto) {

		Long id = tokenUtil.decodeToken(token);
		boolean status = passwordEncoder.matches(passwordDto.getPassword(), passwordDto.getConfirmPassword());
		if (status == true) {
			User user = userRepo.findById(id)
					.orElseThrow(() -> new UserException(404, environment.getProperty("user.resetpassword.user")));
			String encodedpassword = passwordEncoder.encode(passwordDto.getPassword());
			user.setPassword(encodedpassword);
			userRepo.save(user);
		}
		return ResponseHelper.statusResponse(200, "password successfully reset");

	}

	@Override
	public Response uploadImage(String token, MultipartFile imageFile) throws IOException {
		long userId = tokenUtil.decodeToken(token);

		Optional<User> user = userRepo.findById(userId);

		if (!user.isPresent()) {
			throw new UserException(-5, "user is not present");
		}

		UUID uuid = UUID.randomUUID();

		String uniqueId = uuid.toString();
		try {
			Files.copy(imageFile.getInputStream(), fileLocation.resolve(uniqueId), StandardCopyOption.REPLACE_EXISTING);
			user.get().setProfilePic(uniqueId);
			userRepo.save(user.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseHelper.statusResponse(200, "profile picture is uploaded");
	}

	/*
	 * @Override public Response setProfile(String imageFile,String token) throws
	 * IllegalArgumentException, IOException { long
	 * userId=tokenUtil.decodeToken(token); User
	 * user=userRepo.findById(userId).get();
	 * 
	 * user.setProfilePic(imageFile); userRepo.save(user);
	 * 
	 * Response response=ResponseStatus.statusInformation(environment.getProperty(
	 * "status.setProfile.success"),
	 * Integer.parseInt(environment.getProperty("status.success.code"))); return
	 * response; }
	 */

	public Resource getUploadedImageOfUser(String token) {
		long userId = tokenUtil.decodeToken(token);

		Optional<User> user = userRepo.findById(userId);
		if (!user.isPresent()) {
			throw new UserException(-5, "user already exist");
		}

		try {
			Path imageFile = fileLocation.resolve(user.get().getProfilePic());

			Resource resource = new UrlResource(imageFile.toUri());

			if (resource.exists() || (resource.isReadable())) {
				System.out.println(resource);
				return resource;
			} else {
				throw new Exception("Couldn't read file: " + imageFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
