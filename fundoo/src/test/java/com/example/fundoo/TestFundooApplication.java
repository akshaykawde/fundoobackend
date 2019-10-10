package com.example.fundoo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.hibernate.validator.cfg.defs.EmailDef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.notes.dto.NotesDto;
import com.bridgelabz.fundoo.notes.model.Note;
import com.bridgelabz.fundoo.notes.repository.NotesRepo;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.ForgotDto;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.RegisterDto;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepo;
import com.bridgelabz.fundoo.user.service.UserServiceImpl;
import com.bridgelabz.fundoo.utility.RedisUtil;
import com.bridgelabz.fundoo.utility.TokenGenerator;
import com.bridgelabz.fundoo.utility.Utility;

@RunWith(SpringRunner.class)
public class TestFundooApplication {

	@Mock
	private UserRepo userRepo;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	Response statusResponse;

	@Mock
	ResponseToken statusResponseToken;

//	@Mock
//	Optional<?> optional;

	@Mock
	private UserRepo userRepository;

	@Mock
	private ResponseToken responseToken;

	@Mock
	private UserRepo userRepositary;

	@Mock
	private NotesRepo noteRepository;

	@Mock
	private RedisUtil<String> redisUtil;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private TokenGenerator tokenUtil;

	@Mock
	private NotesDto notesDto;

	@Mock
	private Utility utility;

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	Note note = new Note();
	NotesDto noteDto = new NotesDto();

	@Test
	public void noteShouldBeCreated() {
		noteDto.setTitle("title");
		noteDto.setDiscription("discription");
		assertNotNull("title", noteDto.getTitle());
		assertNotNull("discription", noteDto.getDiscription());
		when(tokenUtil.decodeToken(toString())).thenThrow(IllegalStateException.class);
		when(modelMapper.map(notesDto, Note.class)).thenThrow(IllegalStateException.class);

	}

	@Test
	public void onRegisterTest() {
		RegisterDto registerDto = new RegisterDto();
		User user = new User();
		user.setEmailId("akshay.skawde@gmail.com");
		user.setFirstName("akshay");
		user.setLastName("kawde");
		user.setMobileNum("9823605600");
		user.setPassword("akshay");
		when(modelMapper.map(registerDto, User.class)).thenReturn(user);
		when(userRepo.findByEmailId(user.getEmailId())).thenReturn(Optional.ofNullable(user));
		when(passwordEncoder.encode(registerDto.getPassword())).thenReturn(user.getPassword());
		when(userRepo.save(user)).thenReturn(user);
		Response response = userServiceImpl.onRegister(registerDto);
		assertEquals(200, response.getStatusCode());

	}

	@Test
	public void Login() {
		User user = new User();
		user.setEmailId("akshay.skawde@gmail.com");
		user.setPassword("akshay");
		user.setUserId(2L);
		String token = null;
		when(tokenUtil.createToken(user.getUserId())).thenReturn(token);
		assertEquals("akshay", user.getPassword());
		assertEquals("akshay.skawde@gmail.com", user.getEmailId());
		System.out.println("user login_---->" + user);

	}
	
	@Test
	public void userAuthentication() throws UserException, UnsupportedEncodingException
	{
		LoginDTO logindto = new LoginDTO();
		User user = new User();	
		when(modelMapper.map(logindto,User.class)).thenReturn(user);
		logindto.setEmailId("akshay.skawde@gmail.com");
		logindto.setPassword("akshay");
		System.out.println("loginDto is--->"+logindto);
		when(passwordEncoder.matches("akshay", logindto.getPassword())).thenReturn(true);
		assertEquals("akshay.skawde@gmail.com", logindto.getEmailId());	
	}
	
	@Test
	public void forgetPassword()
	{
		ForgotDto emailDto=new ForgotDto();
		User user = new User();
		user.setEmailId("akshay.skawde@gmail.com");
		when(userRepo.findByEmailId(emailDto.getEmailId())).thenReturn(Optional.ofNullable(user));
		Response response = userServiceImpl.forgetPassword(emailDto);
		assertEquals(200, response.getStatusCode());
	}
	
	@Test
	public void resetPassword()
	{
		RegisterDto registerDto = new RegisterDto();
		registerDto.setPassword("password");
		User user = new User();
		when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
		when(userRepo.save(user)).thenReturn(user);
	}

}
