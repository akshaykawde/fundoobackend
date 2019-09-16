package com.bridgelabz.fundoo.user.service;



import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.ForgotDto;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.RegisterDto;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepo;
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


	@Override
	public Response onRegister(RegisterDto userDto) {

		String emailid = userDto.getEmailId();

		User user = modelMapper.map(userDto, User.class);
		System.out.println("user email is--->"+user.getEmailId());
		Optional<User> alreadyPresent = userRepo.findByEmailId(user.getEmailId());
		if (alreadyPresent.isPresent()) {
//			System.out.println("here we go");
			throw new UserException("emailExistError");
		}

	
		String password = passwordEncoder.encode(userDto.getPassword());

		user.setPassword(password);
		user = userRepo.save(user);
		Long userId = user.getUserId();
		System.out.println(emailid + " " + userId);
		System.out.println(emailid+"confirmation mail"+utility.getUrl(userId));
		utility.send(emailid, "confirmation mail", utility.getUrl(userId));

		statusResponse = ResponseHelper.statusResponse(200, "register successfully");
		return statusResponse;

	}
	public ResponseToken onLogin(LoginDTO loginDto) {
		Optional<User> user = userRepo.findByEmailId(loginDto.getEmailId());
		System.out.println(user);
		ResponseToken response = new ResponseToken();
		if (user.isPresent()) {
			System.out.println("password..." + (loginDto.getPassword()));
			
		return authentication(user, loginDto.getPassword());

		}
		else
		{
		
		return response;
		}
	}
	
	@Override
	public Response forgetPassword(ForgotDto emailDto)
	{
		String emailId=emailDto.getEmailId();
		Optional<User>alredyPresent=userRepo.findByEmailId(emailDto.getEmailId());
		if(!alredyPresent.isPresent())
		{
			throw new UserException("user not found");
		}
		ResponseToken response = new ResponseToken();
		long id=alredyPresent.get().getUserId();
		utility.send(emailId, "confirmation mail","http://localhost:4200/resetpassword");
		
		
		return ResponseHelper.statusResponse(200, "check your Email");
		
		
	}
	
	@Override
	public Response resetPassword(String token,String password)
	{
		long id=tokenUtil.decodeToken(token);
		User user=userRepo.findById(id).orElseThrow(()-> new UserException(404,"data not found"));
		String encodedPassword=passwordEncoder.encode(password);
		user.setPassword(encodedPassword);
		user = userRepo.save(user);
		return ResponseHelper.statusResponse(200,"password sucessfully reset");
		
				
	}

	@Override
	public ResponseToken authentication(Optional<User> user, String password) {
	ResponseToken response = new ResponseToken();
		if (user.get().isVerify()) {
			boolean status = passwordEncoder.matches(password, user.get().getPassword());
			if (status == true) {
				System.out.println("logged in");
				String token = tokenUtil.createToken(user.get().getUserId());
				System.out.println(token);
				response.setToken(token);
				response.setStatusCode(200);
				response.setStatusMessage("user.login");
				return response;
			}
			throw new UserException("user.login.password");
		}
		throw new UserException("user.login.verification");
	}

	@Override
	public Response validateEmailId(String token) {
		Long id = tokenUtil.decodeToken(token);
		User user = userRepo.findById(id)
				.orElseThrow(() -> new UserException("user.validation.email"));
		user.setVerify(true);
		userRepo.save(user);
		statusResponse = ResponseHelper.statusResponse(200, ("user.validation"));
		return statusResponse;
	}

}
