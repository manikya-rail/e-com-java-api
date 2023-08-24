package com.ecommerce.techversantInfotech.Authservice.service;

import com.ecommerce.techversantInfotech.Authservice.Dto.AuthRequest;
import com.ecommerce.techversantInfotech.Authservice.Dto.AuthResponse;
import com.ecommerce.techversantInfotech.Authservice.Dto.JwtDto;
import com.ecommerce.techversantInfotech.Authservice.Dto.UserDetails;
import com.ecommerce.techversantInfotech.Authservice.Dto.UserDto;
import com.ecommerce.techversantInfotech.Authservice.Exception.UserAlreadyRegistered;
import com.ecommerce.techversantInfotech.Authservice.Exception.UserNotFoundException;
import com.ecommerce.techversantInfotech.Authservice.JWTutils.JwtService;
import com.ecommerce.techversantInfotech.Authservice.config.CustomUserDetails;
import com.ecommerce.techversantInfotech.Authservice.entity.User;
import com.ecommerce.techversantInfotech.Authservice.enumDetails.UserRole;
import com.ecommerce.techversantInfotech.Authservice.repository.UserCredential;
import com.ecommerce.techversantInfotech.Authservice.utils.ImageProcessingUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.Mock;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
	@Value("${app.filename}")
	private String filePath;

	@Mock
	private UserCredential userCredential;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtService jwtService;

	@Mock
	private UserDetailsService userDetailsService;

	@InjectMocks
	AuthService authService = new AuthServiceImpl();

	private final Logger logger = LoggerFactory.getLogger(AuthServiceImplTest.class);
	private final String userJsonStringSuper = "{" + "\"name\":\"jenob\"," + "\"username\":\"jeb\","
			+ "\"email\":\"jenob@gmail.com\"," + "\"password\":\"12345\"," + "\"location\":\"kochi\","
			+ "\"description\":\"Hi this test purpose\"," + "\"mobileNumber\":\"9840280239\","
			+ "\"role\":\"SUPER_ADMIN\"" + "}";

	// Common UserDto
	private final UserDto commonUserDtoSuper = UserDto.builder().name("jenob").username("jeb").email("jenob@gmail.com")
			.Password("12345").location("kochi").Description("Hi this test purpose").mobileNumber("9840280239")
			.role("SUPER_ADMIN").build();

	// Common User entity
	private final User commonNewUserSuper = User.builder().name("jenob").username("jeb").password("12345")
			.email("jenob@gmail.com").mobileNumber("9840280239").Description("Hi this test purpose").location("kochi")
			.role(UserRole.SUPER_ADMIN).active(true).delete(false).modifiedOn(null).createOn(new Date()).build();
	// Common User JSON String
	private final String userJsonString = "{" + "\"name\":\"jenob\"," + "\"username\":\"jeb\","
			+ "\"email\":\"jenob@gmail.com\"," + "\"password\":\"12345\"," + "\"location\":\"kochi\","
			+ "\"description\":\"Hi this test purpose\"," + "\"mobileNumber\":\"9840280239\"," + "\"role\":\"ADMIN\""
			+ "}";

	// Common UserDto
	private final UserDto commonUserDto = UserDto.builder().name("jenob").username("jeb").email("jenob@gmail.com")
			.Password("12345").location("kochi").Description("Hi this test purpose").mobileNumber("9840280239")
			.role("ADMIN").build();

	// Common User entity
	private final User commonNewUser = User.builder().name("jenob").username("jeb").password("12345")
			.email("jenob@gmail.com").mobileNumber("9840280239").Description("Hi this test purpose").location("kochi")
			.role(UserRole.ADMIN).active(true).delete(false).modifiedOn(null).createOn(new Date()).build();

	// Common MockMultipartFile for image testing
	private final MockMultipartFile testImageFile = new MockMultipartFile("file", "test-image.jpg", "image/jpeg",
			new byte[0]);

	// Common User ID for existing user testing
	private final int existingUserId = 1;

	// Common existing user entity for testing
	private final User existingUser = createUser("Initial Name", "initialUsername", "initialemail@gmail.com", "12345",
			"1234567890", "Initial description", "Initial location", UserRole.ADMIN);

	@BeforeEach
	public void setup() {
		// Mock the value of app.filename
		filePath = "c:/Users/Nayana Jayaraj/Documents/Images/"; // Replace with the actual path
	}

	@Test
	void negative_registerTest() {
		try (MockedStatic<ImageProcessingUtils> mockedStatic = Mockito.mockStatic(ImageProcessingUtils.class)) {
			mockedStatic.when(() -> ImageProcessingUtils.convertObject(userJsonStringSuper))
					.thenReturn(commonUserDtoSuper);
			when(userCredential.findByEmail(commonUserDtoSuper.getEmail())).thenReturn(Optional.of(commonNewUserSuper));
			Assertions.assertThrows(UserAlreadyRegistered.class,
					() -> authService.saveUser(userJsonStringSuper, testImageFile));
		}
	}

	@Test
	void positive_userRegister_case() {
		try (MockedStatic<ImageProcessingUtils> mockedStatic = Mockito.mockStatic(ImageProcessingUtils.class)) {
			mockedStatic.when(() -> ImageProcessingUtils.convertObject(userJsonStringSuper))
					.thenReturn(commonUserDtoSuper);

			when(userCredential.findByEmail(commonUserDtoSuper.getEmail())).thenReturn(Optional.empty());
			authService.saveUser(userJsonStringSuper, testImageFile);
			verify(userCredential).save(any(User.class));
		}
	}

	@Test
	void negative_clientRegisterTest() {
		try (MockedStatic<ImageProcessingUtils> mockedStatic = Mockito.mockStatic(ImageProcessingUtils.class)) {
			mockedStatic.when(() -> ImageProcessingUtils.convertObject(userJsonString)).thenReturn(commonUserDto);

			when(userCredential.findByEmail(commonUserDto.getEmail())).thenReturn(Optional.of(commonNewUser));
			Assertions.assertThrows(UserAlreadyRegistered.class,
					() -> authService.clientRegister(userJsonString, testImageFile));
		}
	}

	@Test
	void positive_clientRegister_case() {
		try (MockedStatic<ImageProcessingUtils> mockedStatic = Mockito.mockStatic(ImageProcessingUtils.class)) {
			mockedStatic.when(() -> ImageProcessingUtils.convertObject(userJsonString)).thenReturn(commonUserDto);

			when(userCredential.findByEmail(commonUserDto.getEmail())).thenReturn(Optional.empty());
			authService.clientRegister(userJsonString, testImageFile);
			verify(userCredential).save(any(User.class));
		}
	}

	@Test
	public void testGetUserById_UserFound() {
		int userId = 0;

		// Mock behavior of userCredential.findById
		when(userCredential.findById(userId)).thenReturn(Optional.of(commonNewUser));

		// Test the method
		User result = authService.getUserById(userId);

		logger.info("User Details:");
		logger.info("ID: " + result.getId());
		logger.info("Name: " + result.getName());
		logger.info("Email: " + result.getEmail());
		// Verify the interactions and assertions
		verify(userCredential, times(1)).findById(userId);
		assertNotNull(result);
		assertEquals(userId, result.getId()); // Add more specific assertions for other properties
	}

	@Test
	public void testGetUserById_UserNotFound() {
		int userId = 0;

		// Mock behavior of userCredential.findById
		when(userCredential.findById(userId)).thenReturn(Optional.empty());

		// Test and verify that UserNotFoundException is thrown
		assertThrows(UserNotFoundException.class, () -> authService.getUserById(userId));
		verify(userCredential, times(1)).findById(userId);
	}

	@Test
	public void testGetAllClients_Positive() {
		List<User> userList = createTestUserList(2, UserRole.ADMIN);

		// Mock behavior of userCredential.findAll
		when(userCredential.findAll()).thenReturn(userList);

		// Test the method
		List<User> clients = authService.getAllClients();
		assertClientList(clients, 2);
	}

	@Test
	public void testGetAllClients_NoClientsFound() {
		// Mock behavior of userCredential.findAll
		when(userCredential.findAll()).thenReturn(new ArrayList<>());

		// Test and verify that UserNotFoundException is thrown
		assertThrows(UserNotFoundException.class, () -> authService.getAllClients());
		verify(userCredential, times(1)).findAll();
	}

	@Test
	public void testGetAllClients_WithSuperAdmin() {
		List<User> userList = createTestUserList(1, UserRole.SUPER_ADMIN);

		// Mock behavior of userCredential.findAll
		when(userCredential.findAll()).thenReturn(userList);

		// Test the method
		List<User> clients = authService.getAllClients();
		assertClientList(clients, 0);
	}

	private List<User> createTestUserList(int count, UserRole role) {
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			userList.add(createUser("jenob" + i, "jeb", "jenob@gmail.com", "12345", "984028023" + i,
					"Hi this test purpose", "kochi", role));
		}
		return userList;
	}

	private void assertClientList(List<User> clients, int expectedSize) {
		logger.info("Clients List:");
		for (User client : clients) {
			logger.info("User ID: {}", client.getId());
			logger.info("User Name: {}", client.getName());
			// Log other properties as needed
		}

		verify(userCredential, times(1)).findAll();
		assertEquals(expectedSize, clients.size()); // Ensure the correct number of clients is returned
	}

	@Test
	public void testDownloadImage_Positive() throws FileNotFoundException {
		User userWithImage = createUserWithImage("jenob", "766f97e384e6cd3da3f1d1367b5aa986.jpg");
		int userId = 0;
		mockUserFindById(userId, Optional.of(userWithImage));

		InputStream imageStream = authService.downloadImage(userId);
		verifyAndAssertImageDownload(userId, imageStream);
	}

	@Test
	public void testDownloadImage_NoImage() throws FileNotFoundException {
		User userWithoutImage = createUserWithoutImage("jeb");
		int userId = 0;
		mockUserFindById(userId, Optional.of(userWithoutImage));

		InputStream imageStream = authService.downloadImage(userId);
		verifyAndAssertNoImage(userId, imageStream);
	}

	@Test
	public void testDownloadImage_UserNotFound() {
		int userId = 0;
		mockUserFindById(userId, Optional.empty());

		assertThrows(UserNotFoundException.class, () -> authService.downloadImage(userId));
		verify(userCredential, times(1)).findById(userId);
	}

	private void mockUserFindById(int userId, Optional<User> user) {
		when(userCredential.findById(userId)).thenReturn(user);
	}

	private void verifyAndAssertImageDownload(int userId, InputStream imageStream) {
		verify(userCredential, times(1)).findById(userId);
		assertNotNull(imageStream);
		logger.info("Image stream retrieved successfully.");
	}

	private void verifyAndAssertNoImage(int userId, InputStream imageStream) {
		verify(userCredential, times(1)).findById(userId);
		assertNull(imageStream);
	}

	private User createUserWithImage(String name, String imagePath) {
		return User.builder().name(name).image(filePath + imagePath).build();
	}

	private User createUserWithoutImage(String name) {
		return User.builder().name(name).build();
	}

	@Test
	public void testDeleteClient_Positive() {
		int userId = 0;
		mockUserExistsById(userId, true);

		String result = authService.deleteClient(userId);

		verifyAndAssertDeleteClient(userId, result);
	}

	@Test
	public void testDeleteClient_UserNotFound() {
		int userId = 0;
		mockUserExistsById(userId, false);

		assertThrows(UserNotFoundException.class, () -> authService.deleteClient(userId));
		verifyAndAssertUserNotFound(userId);
	}

	private void mockUserExistsById(int userId, boolean exists) {
		when(userCredential.existsById(userId)).thenReturn(exists);
	}

	private void verifyAndAssertDeleteClient(int userId, String result) {
		verify(userCredential, times(1)).existsById(userId);
		verify(userCredential, times(1)).deleteById(userId);
		assertEquals("Delete client successfully", result);
		logger.info("Delete client successfully");
	}

	private void verifyAndAssertUserNotFound(int userId) {
		verify(userCredential, times(1)).existsById(userId);
		verify(userCredential, times(0)).deleteById(userId); // Ensure delete is not called
	}

	@Test
	public void negative_updateClientTest_UserNotFound() {
		int userId = 1;
		String updateUserJson = createUpdateUserJson("Updated Name", "updatedUsername", "updatedemail@gmail.com",
				"9876543210", "Updated description", "Updated location");
		mockUserNotFound(userId);

		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
			authService.updateClient(updateUserJson, testImageFile, userId);
		});

		verifyAndAssertUserNotFound(exception, userId);
	}

	@Test
	public void positive_updateClientTest_UserFound() {
		int userId = 1;
		String updateUserJson = createUpdateUserJson("Updated Name", "updatedUsername", "updatedemail@gmail.com",
				"9876543210", "Updated description", "Updated location");
		User existingUser = createUser("Initial Name", "initialUsername", "initialemail@gmail.com", "12345",
				"1234567890", "Initial description", "Initial location", UserRole.ADMIN);
		mockUserFound(userId, existingUser);

		String result = authService.updateClient(updateUserJson, testImageFile, userId);

		verifyAndAssertUserUpdate(existingUser, result);
	}

	private void mockUserNotFound(int userId) {
		when(userCredential.findById(userId)).thenReturn(Optional.empty());
	}

	private void mockUserFound(int userId, User existingUser) {
		when(userCredential.findById(userId)).thenReturn(Optional.of(existingUser));
	}

	private String createUpdateUserJson(String name, String username, String email, String mobileNumber,
			String description, String location) {
		return "{" + "\"name\":\"" + name + "\"," + "\"username\":\"" + username + "\"," + "\"email\":\"" + email
				+ "\"," + "\"mobileNumber\":\"" + mobileNumber + "\"," + "\"description\":\"" + description + "\","
				+ "\"location\":\"" + location + "\"" + "}";
	}

	private void verifyAndAssertUserNotFound(UserNotFoundException exception, int userId) {
		assertEquals("USER_NOT_FOUND", exception.getErrorCode());
		assertEquals("User is not found", exception.getMessage());
		verify(userCredential, times(1)).findById(userId);
	}

	private void verifyAndAssertUserUpdate(User existingUser, String result) {
		verify(userCredential).save(existingUser);
		assertEquals("updated successfully", result);
		logger.info(result, "updated successfully");
	}

	// Helper method to create a User with test data
	private User createUser(String name, String username, String email, String password, String mobileNumber,
			String description, String location, UserRole role) {
		return User.builder().name(name).username(username).email(email).password(password).mobileNumber(mobileNumber)
				.Description(description).location(location).role(role).active(true).delete(false).modifiedOn(null)
				.createOn(new Date()).build();
	}

}