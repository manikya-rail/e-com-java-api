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
	
	@BeforeEach
    public void setup() {
        // Mock the value of app.filename
        filePath = "c:/Users/Nayana Jayaraj/Documents/Images/"; // Replace with the actual path
    }
	@Test
	void negative_registerTest() {
		String expectedJsonString = "{" + "\"name\":\"jenob\"," + "\"username\":\"jeb\","
				+ "\"email\":\"jenob@gmail.com\"," + "\"password\":\"12345\"," + "\"location\":\"kochi\","
				+ "\"description\":\"Hi this test purpose\"," + "\"mobileNumber\":\"9840280239\","
				+ "\"role\":\"SUPER_ADMIN\"" + "}";
		MockMultipartFile file = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new byte[0]);

		UserDto userDto = UserDto.builder().name("jenob").username("jeb").email("jenob@gmail.com").Password("12345")
				.location("kochi").Description("Hi this test purpose").mobileNumber("9840280239").role("SUPER_ADMIN")
				.build();

		User newUser = User.builder().name("jenob").username("jeb").password("12345").email("jenob@gmail.com")
				.mobileNumber("9840280239").Description("Hi this test purpose").location("kochi")
				.role(UserRole.valueOf("SUPER_ADMIN")).active(true).delete(false).modifiedOn(null).createOn(new Date())
				.build();

		try (MockedStatic<ImageProcessingUtils> mockedStatic = Mockito.mockStatic(ImageProcessingUtils.class)) {
			mockedStatic.when(() -> ImageProcessingUtils.convertObject(expectedJsonString)).thenReturn(userDto);

			when(userCredential.findByEmail("jenob@gmail.com")).thenReturn(Optional.of(newUser));
			Assertions.assertThrows(UserAlreadyRegistered.class, () -> authService.saveUser(expectedJsonString, file));
		}

	}

	@Test
	void positive_userRegister_case() {
		String expectedJsonString = "{" + "\"name\":\"jenob\"," + "\"username\":\"jeb\","
				+ "\"email\":\"jenob@gmail.com\"," + "\"password\":\"12345\"," + "\"location\":\"kochi\","
				+ "\"description\":\"Hi this test purpose\"," + "\"mobileNumber\":\"9840280239\","
				+ "\"role\":\"SUPER_ADMIN\"" + "}";
		MockMultipartFile file = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new byte[0]);

		UserDto userDto = UserDto.builder().name("jenob").username("jeb").email("jenob@gmail.com").Password("12345")
				.location("kochi").Description("Hi this test purpose").mobileNumber("9840280239").role("SUPER_ADMIN")
				.build();

		User newUser = User.builder().name("jenob").username("jeb").password("12345").email("jenob@gmail.com")
				.mobileNumber("9840280239").Description("Hi this test purpose").location("kochi")
				.role(UserRole.valueOf("SUPER_ADMIN")).active(true).delete(false).modifiedOn(null).createOn(new Date())
				.build();

		try (MockedStatic<ImageProcessingUtils> mockedStatic = Mockito.mockStatic(ImageProcessingUtils.class)) {
			mockedStatic.when(() -> ImageProcessingUtils.convertObject(expectedJsonString)).thenReturn(userDto);

			when(userCredential.findByEmail("jenob@gmail.com")).thenReturn(Optional.empty());
			authService.saveUser(expectedJsonString, file);
			verify(userCredential).save(any(User.class));

		}
	}

	@Test
	void negative_clientRegisterTest() {
		String expectedJsonString = "{" + "\"name\":\"jenob\"," + "\"username\":\"jeb\","
				+ "\"email\":\"jenob@gmail.com\"," + "\"password\":\"12345\"," + "\"location\":\"kochi\","
				+ "\"description\":\"Hi this test purpose\"," + "\"mobileNumber\":\"9840280239\","
				+ "\"role\":\"ADMIN\"" + "}";
		MockMultipartFile file = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new byte[0]);

		UserDto userDto = UserDto.builder().name("jenob").username("jeb").email("jenob@gmail.com").Password("12345")
				.location("kochi").Description("Hi this test purpose").mobileNumber("9840280239").role("ADMIN").build();

		User newUser = User.builder().name("jenob").username("jeb").password("12345").email("jenob@gmail.com")
				.mobileNumber("9840280239").Description("Hi this test purpose").location("kochi")
				.role(UserRole.valueOf("ADMIN")).active(true).delete(false).modifiedOn(null).createOn(new Date())
				.build();

		try (MockedStatic<ImageProcessingUtils> mockedStatic = Mockito.mockStatic(ImageProcessingUtils.class)) {
			mockedStatic.when(() -> ImageProcessingUtils.convertObject(expectedJsonString)).thenReturn(userDto);

			when(userCredential.findByEmail("jenob@gmail.com")).thenReturn(Optional.of(newUser));
			Assertions.assertThrows(UserAlreadyRegistered.class, () -> authService.saveUser(expectedJsonString, file));
		}

	}

	@Test
	void positive_clientRegister_case() {
		String expectedJsonString = "{" + "\"name\":\"jenob\"," + "\"username\":\"jeb\","
				+ "\"email\":\"jenob@gmail.com\"," + "\"password\":\"12345\"," + "\"location\":\"kochi\","
				+ "\"description\":\"Hi this test purpose\"," + "\"mobileNumber\":\"9840280239\","
				+ "\"role\":\"ADMIN\"" + "}";
		MockMultipartFile file = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new byte[0]);

		UserDto userDto = UserDto.builder().name("jenob").username("jeb").email("jenob@gmail.com").Password("12345")
				.location("kochi").Description("Hi this test purpose").mobileNumber("9840280239").role("ADMIN").build();

		User newUser = User.builder().name("jenob").username("jeb").password("12345").email("jenob@gmail.com")
				.mobileNumber("9840280239").Description("Hi this test purpose").location("kochi")
				.role(UserRole.valueOf("ADMIN")).active(true).delete(false).modifiedOn(null).createOn(new Date())
				.build();

		try (MockedStatic<ImageProcessingUtils> mockedStatic = Mockito.mockStatic(ImageProcessingUtils.class)) {
			mockedStatic.when(() -> ImageProcessingUtils.convertObject(expectedJsonString)).thenReturn(userDto);

			when(userCredential.findByEmail("jenob@gmail.com")).thenReturn(Optional.empty());
			authService.saveUser(expectedJsonString, file);
			verify(userCredential).save(any(User.class));

		}
	}

	@Test
	public void testGetUserById_UserFound() {
		// Create a User instance with test data
		User newUser = createUser("jenob", "jeb", "jenob@gmail.com", "12345", "9840280239", "Hi this test purpose",
				"kochi", UserRole.SUPER_ADMIN);

		int userId = 0;

		// Mock behavior of userCredential.findById
		when(userCredential.findById(userId)).thenReturn(Optional.of(newUser));

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
		// Create some test users
		User user1 = createUser("jenob1", "jeb", "jenob@gmail.com", "12345", "9840280238", "Hi this test purpose",
				"kochi", UserRole.ADMIN);
		User user2 = createUser("jenob", "jeb", "jen@gmail.com", "12345", "9840280239", "Hi this test purpose", "kochi",
				UserRole.ADMIN);

		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);

		// Mock behavior of userCredential.findAll
		when(userCredential.findAll()).thenReturn(userList);

		// Test the method
		List<User> clients = authService.getAllClients();
		logger.info("Clients List:");
		for (User client : clients) {
			logger.info("User ID: {}", client.getId());
			logger.info("User Name: {}", client.getName());
			// Log other properties as needed
		}
		// Verify the interactions and assertions
		verify(userCredential, times(1)).findAll();
		assertEquals(2, clients.size()); // Ensure the correct number of clients is returned
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
		// Create a test user with SUPER_ADMIN role
		User superAdminUser = createUser("jenob", "jeb", "jenob@gmail.com", "12345", "9840280239",
				"Hi this test purpose", "kochi", UserRole.SUPER_ADMIN);

		List<User> userList = new ArrayList<>();
		userList.add(superAdminUser);

		// Mock behavior of userCredential.findAll
		when(userCredential.findAll()).thenReturn(userList);

		// Test the method
		List<User> clients = authService.getAllClients();

		// Verify the interactions and assertions
		verify(userCredential, times(1)).findAll();
		assertEquals(0, clients.size()); // Ensure no clients are returned
	}

	@Test
    public void testDownloadImage_Positive() throws FileNotFoundException {
        // Create a User instance with an image
        User userWithImage = createUserWithImage("jenob", "766f97e384e6cd3da3f1d1367b5aa986.jpg");

        int userId = 0;

        // Mock behavior of userCredential.findById
        when(userCredential.findById(userId)).thenReturn(Optional.of(userWithImage));

        // Test the method
        InputStream imageStream = authService.downloadImage(userId);

        // Verify the interactions and assertions
        verify(userCredential, times(1)).findById(userId);
        assertNotNull(imageStream);
        
        logger.info("Image stream retrieved successfully.");
    }

    @Test
    public void testDownloadImage_NoImage() throws FileNotFoundException {
        // Create a User instance without an image
        User userWithoutImage = createUserWithoutImage("jeb");

        int userId = 0;

        // Mock behavior of userCredential.findById
        when(userCredential.findById(userId)).thenReturn(Optional.of(userWithoutImage));

        // Test the method
        InputStream imageStream = authService.downloadImage(userId);

        // Verify the interactions and assertions
        verify(userCredential, times(1)).findById(userId);
        assertNull(imageStream);
    }

    @Test
    public void testDownloadImage_UserNotFound() {
        int userId = 0;

        // Mock behavior of userCredential.findById
        when(userCredential.findById(userId)).thenReturn(Optional.empty());

        // Test and verify that UserNotFoundException is thrown
        assertThrows(UserNotFoundException.class, () -> authService.downloadImage(userId));
        verify(userCredential, times(1)).findById(userId);
    }

    private User createUserWithImage(String name, String imagePath) {
        return User.builder()
                .name(name)
                .image(filePath+imagePath)
                .build();
    }

    private User createUserWithoutImage(String name) {
        return User.builder()
                .name(name)
                .build();
    }
    @Test
    public void testDeleteClient_Positive() {
        int userId = 0;

        // Mock behavior of userCredential.existsById
        when(userCredential.existsById(userId)).thenReturn(true);

        // Test the method
        String result = authService.deleteClient(userId);

        // Verify the interactions and assertions
        verify(userCredential, times(1)).existsById(userId);
        verify(userCredential, times(1)).deleteById(userId);
        assertEquals("Delete client successfully", result);
        
        logger.info("Delete client successfully");
    }

    @Test
    public void testDeleteClient_UserNotFound() {
        int userId = 0;

        // Mock behavior of userCredential.existsById
        when(userCredential.existsById(userId)).thenReturn(false);

        // Test and verify that UserNotFoundException is thrown
        assertThrows(UserNotFoundException.class, () -> authService.deleteClient(userId));
        verify(userCredential, times(1)).existsById(userId);
        verify(userCredential, times(0)).deleteById(userId); // Ensure delete is not called
    }
	// Helper method to create a User with test data
	private User createUser(String name, String username, String email, String password, String mobileNumber,
			String description, String location, UserRole role) {
		return User.builder().name(name).username(username).email(email).password(password).mobileNumber(mobileNumber)
				.Description(description).location(location).role(role).active(true).delete(false).modifiedOn(null)
				.createOn(new Date()).build();
	}
	    
	    @Test
	    public void negative_updateClientTest_UserNotFound() {
	        // Arrange
	    	 String updateUserJson = "{" +
		                "\"name\":\"Updated Name\"," +
		                "\"username\":\"updatedUsername\"," +
		                "\"email\":\"updatedemail@gmail.com\"," +
		                "\"mobileNumber\":\"9876543210\"," +
		                "\"description\":\"Updated description\"," +
		                "\"location\":\"Updated location\"" +
		                "}";
	    	MockMultipartFile file = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new byte[0]);
	        int userId = 1;
	        when(userCredential.findById(userId)).thenReturn(Optional.empty());
	        
	        // Act and Assert
	        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
	            authService.updateClient(updateUserJson, file, userId);
	        });

	        assertEquals("USER_NOT_FOUND", exception.getErrorCode());
	        assertEquals("User is not found", exception.getMessage());
	    }

	    @Test
	    public void positive_updateClientTest_UserFound() {
	        // Arrange
	        int userId = 1;
	        String updateUserJson = "{" +
	                "\"name\":\"Updated Name\"," +
	                "\"username\":\"updatedUsername\"," +
	                "\"email\":\"updatedemail@gmail.com\"," +
	                "\"mobileNumber\":\"9876543210\"," +
	                "\"description\":\"Updated description\"," +
	                "\"location\":\"Updated location\"" +
	                "}";
	        User existingUser = new User();
	        User userToUpdate = createUser("Initial Name", "initialUsername", "initialemail@gmail.com", "12345",
	                "1234567890", "Initial description", "Initial location", UserRole.ADMIN);
	        MockMultipartFile file = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new byte[0]);
	        // Ensure that when you call findById, it returns the existing user
	        when(userCredential.findById(userId)).thenReturn(Optional.of(userToUpdate));

	        // Act
	        String result = authService.updateClient(updateUserJson, file, userId);
	        logger.info("Test result: {}", result);
	        // Assert
	        assertEquals("updated successfully", result);
	        
	        // Verify that save was called with the modified user
	        verify(userCredential).save(any(User.class));
	        logger.info(result,"updated successfully");
	       
	    }
	    
	    @Test
	    public void testLogin_Success() {
	        // Mock User data
	    	 // Mock User data
	    	User user = new User();
	        user.setId(1);
	        user.setUsername("johndoe@gmail.com");
	        user.setPassword("$2a$10$RzP4x4mTTUvd9fw/j7RdA.7amCX4/ZM0bV4MZUq5Z4AVM1ns4Y6qm"); // Encrypted password

	        // Mock Authentication
	        Authentication authentication = new UsernamePasswordAuthenticationToken("johndoe@gmail.com", "password");
	        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

	        // Mock UserDetails
	        CustomUserDetails userDetails = new CustomUserDetails(user);
	        when(userDetailsService.loadUserByUsername("johndoe@gmail.com")).thenReturn(userDetails);

	        // Mock JWT token generation
	        JwtDto jwtDto = new JwtDto(userDetails.getId(), userDetails.getUsername(), null, userDetails.getRole());
	        when(jwtService.generateToken(jwtDto)).thenReturn("generated_token");

	     // Create AuthRequest
	        AuthRequest authRequest = new AuthRequest("johndoe@gmail.com", "password", null);

	        // Perform login
	        AuthResponse authResponse = authService.login(authRequest);

	        // Assertions
	        UserDetails userDetailsDto = authResponse.getUserDetails();
	        assert userDetailsDto != null;
	        assert userDetailsDto.getId() == userDetails.getId();
	        assert userDetailsDto.getUsername().equals(userDetails.getUsername());
	        //assert userDetailsDto.getRole() == userDetails.getRole();
	        assert authResponse.getToken().equals("generated_token");
	    }

	    @Test
	    public void testLogin_UserNotFound() {
	        // Mock Authentication
	        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);

	        // Create AuthRequest
	        AuthRequest authRequest = new AuthRequest("nonexistent", "password", null);

	        // Test and verify that UserNotFoundException is thrown
	        UserNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> {
	            authService.login(authRequest);
	        });

	        assert exception.getErrorCode().equals("USER_NOT_FOUND");
	        assert exception.getMessage().equals("User not found");
	    }
}