package com.bridgelabz.fundoo.user.service;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.ForgotDto;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.RegisterDto;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepo;
import com.bridgelabz.fundoo.utility.RedisUtil;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenGenerator;
import com.bridgelabz.fundoo.utility.Utility;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenGenerator tokenUtil;

	@Autowired
	private Response statusResponse;

	@Autowired
	private Utility utility;

	@Autowired
	private RedisUtil<String> redisUtil;

	@Autowired
	private RedisTemplate<String, Object> redistemplate;

	@Value("${Key}")
	private String key;
	private final Path fileLocation = Paths.get("/home/admin94/Wallpapers/");

	@Override
	public Response onRegister(RegisterDto userDto) {
		String emailid = userDto.getEmailId();
		User user = modelMapper.map(userDto, User.class);
		System.out.println("user email is--->" + user.getEmailId());
		Optional<User> alreadyPresent = userRepo.findByEmailId(user.getEmailId());
		if (alreadyPresent.isPresent()) {
			throw new UserException("emailExistError");
		}
		String password = passwordEncoder.encode(userDto.getPassword());
		user.setPassword(password);
		user = userRepo.save(user);
		Long userId = user.getUserId();
		System.out.println(emailid + " " + userId);
		System.out.println(emailid + "confirmation mail" + Utility.getUrl(userId));

		String url;
		url = Utility.getUrl(userId);
		System.out.println(url);
		utility.send(emailid, "confirmation mail", Utility.getUrl(userId));
		statusResponse = ResponseHelper.statusResponse(200, "register successfully");
		return statusResponse;
	}

	public ResponseToken onLogin(LoginDTO loginDto) {
		Optional<User> user = userRepo.findByEmailId(loginDto.getEmailId());
		System.out.println(user);
		ResponseToken response = new ResponseToken();
		if (user.isPresent()) {
			System.out.println("password..." + (loginDto.getPassword()));
			redistemplate.opsForHash().put(key, loginDto.getEmailId(), tokenUtil.createToken(user.get().getUserId()));

			List<String> array = new ArrayList<String>();
			array.add(user.get().getEmailId());
			array.add(user.get().getFirstName());
			array.add(user.get().getLastName());
			array.add(user.get().getProfilePic());
			redistemplate.opsForValue().set(key, array);
			return authentication(user, loginDto.getPassword());

		} else {

			return response;
		}
	}

	@Override
	public Response forgetPassword(ForgotDto emailDto) {
		String emailId = emailDto.getEmailId();
		Optional<User> alredyPresent = userRepo.findByEmailId(emailDto.getEmailId());
		if (!alredyPresent.isPresent()) {
			throw new UserException("user not found");
		}
		utility.send(emailId, "confirmation mail", "http://localhost:4200/resetpassword");

		return ResponseHelper.statusResponse(200, "check your Email");

	}

	@Override
	public Response resetPassword(String token, String password) {
		long id = tokenUtil.decodeToken(token);
		User user = userRepo.findById(id).orElseThrow(() -> new UserException(404, "data not found"));
		String encodedPassword = passwordEncoder.encode(password);
		user.setPassword(encodedPassword);
		user = userRepo.save(user);
		return ResponseHelper.statusResponse(200, "password sucessfully reset");

	}

	@Override
	public ResponseToken authentication(Optional<User> user, String password) {
		ResponseToken response = new ResponseToken();
		if (user.get().isVerify()) {
			boolean status = passwordEncoder.matches(password, user.get().getPassword());
			if (status == true) {
				String token = tokenUtil.createToken(user.get().getUserId());
				redisUtil.putMap("token", token);
			}
			response.setStatusCode(200);
			response.setStatusMessage("user.login");

			response.setToken(redisUtil.getMap("token"));
			return response;
		}
		throw new UserException("user.login.password");
	}

	@Override
	public Response validateEmailId(String token) {
		Long id = tokenUtil.decodeToken(token);
		User user = userRepo.findById(id).orElseThrow(() -> new UserException("user.validation.email"));
		user.setVerify(true);
		userRepo.save(user);
		statusResponse = ResponseHelper.statusResponse(200, ("user.validation"));
		return statusResponse;
	}

	@Override
	public Response uploadImage(String token, MultipartFile imageFile) {
		long userId = tokenUtil.decodeToken(token);
		Optional<User> user = userRepo.findById(userId);
		if (!user.isPresent()) {
			throw new UserException(-5, "user is not present");
		} else {
			String filename = StringUtils.cleanPath(imageFile.getOriginalFilename());
			try {
				Files.copy(imageFile.getInputStream(), fileLocation.resolve(filename),
						StandardCopyOption.REPLACE_EXISTING);
				user.get().setProfilePic(filename);
				userRepo.save(user.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ResponseHelper.statusResponse(200, "profile picture is uploaded");
		}
	}

	@Override
	public Resource getUploadedImageOfUser(String token) throws MalformedURLException {
		Long id = tokenUtil.decodeToken(token);
		Optional<User> user = userRepo.findById(id);
		Path imageFile = fileLocation.resolve(user.get().getProfilePic());
		Resource resource = new UrlResource(imageFile.toUri());
		return resource;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> showProfilePic() {

		return (List<String>) redistemplate.opsForValue().get(key);
	}
}
