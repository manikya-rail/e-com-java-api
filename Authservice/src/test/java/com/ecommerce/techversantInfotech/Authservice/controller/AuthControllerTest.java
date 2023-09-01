package com.ecommerce.techversantInfotech.Authservice.controller;


import com.ecommerce.techversantInfotech.Authservice.Entity.Test_User;
import com.ecommerce.techversantInfotech.Authservice.Exception.UserAlreadyRegistered;
import com.ecommerce.techversantInfotech.Authservice.enumDetails.UserRole;
import com.ecommerce.techversantInfotech.Authservice.repository.TestRepo;
import com.ecommerce.techversantInfotech.Authservice.service.AuthService;
import com.fasterxml.jackson.databind.JsonNode;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
class AuthControllerTest{

    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRepo testRepo;
    
    @MockBean
    private AuthService authService;

    @BeforeEach
    public void setUp(){
        mockMvc= MockMvcBuilders.standaloneSetup(authController).build();
    }

    @AfterEach
    public void tearDown(){
        testRepo.deleteAll();
    }



    private final String userJsonStringSuper = "{" + "\"name\":\"jenob\"," + "\"username\":\"jeb\","
            + "\"email\":\"jenob@gmail.com\"," + "\"password\":\"12345\"," + "\"location\":\"kochi\","
            + "\"description\":\"Hi this test purpose\"," + "\"mobileNumber\":\"9840280239\","
            + "\"role\":\"SUPER_ADMIN\"" + "}";

    private final String client_userJsonStringSuper = "{" + "\"name\":\"jeb\"," + "\"username\":\"jb\","
            + "\"email\":\"jen@gmail.com\"," + "\"password\":\"12345\"," + "\"location\":\"kochi\","
            + "\"description\":\"Hi this test purpose\"," + "\"mobileNumber\":\"98402839\","
            + "\"role\":\"ADMIN\"" + "}";


    private final MockMultipartFile testImageFile = new MockMultipartFile("file", "test-image.jpg", "image/jpeg",
            new byte[0]);

    @Test
    public void testRegistration() throws Exception {
       String userDtoJson=objectMapper.writeValueAsString(userJsonStringSuper);

       //Mocking the request
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/api/auth/register")
                .file(testImageFile)
                .param("user",userJsonStringSuper)
                .contentType(MULTIPART_FORM_DATA);

         MvcResult result= mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                 .andReturn();
      //Retrieving  response
        String responseContent = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        JsonNode userId = responseJson.get("id");
        int user_id=responseJson.get("id").asInt();

        Optional<Test_User> user=testRepo.findById(user_id);

        assertEquals("jenob",user.get().getName());
        assertEquals("jeb",user.get().getUsername());

    }
    @Test
    public void testRegistrationNegative() throws Exception {
        // Prepare the database state by inserting a user with the same details
        Test_User user=Test_User.builder()
                .name("jenob")
                .username("jeb")
                .email("jenob@gmail.com")
                .password("12345")
                .location("kochi")
                .Description("Hi this is test purpose")
                .mobileNumber("9840280239")
                .role(UserRole.valueOf("SUPER_ADMIN"))
                .build();

        testRepo.save(user);

        // Attempt to register a user with the same details
        String userDtoJson = objectMapper.writeValueAsString(userJsonStringSuper);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/api/auth/register")
                .file(testImageFile)
                .param("user", userJsonStringSuper)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        assertThrows(Exception.class, () -> {
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest());  // Expecting a 400 Bad Request
        });



    }
    @Test
    public void clientRegistration() throws Exception {
        String userDtoJson=objectMapper.writeValueAsString(client_userJsonStringSuper);

        //Mocking the request
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/api/auth/register")
                .file(testImageFile)
                .param("user",userJsonStringSuper)
                .contentType(MULTIPART_FORM_DATA);

        MvcResult result= mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();
        //Retrieving  response
        String responseContent = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        JsonNode userId = responseJson.get("id");
        int user_id=responseJson.get("id").asInt();

        Optional<Test_User> user=testRepo.findById(user_id);

        assertEquals("jenob",user.get().getName());
        assertEquals("jeb",user.get().getUsername());

    }

    @Test
    public void testGetUserById() throws Exception {
        // Prepare a test user
        Test_User testUser = Test_User.builder()
                .id(1)
                .name("jenob")
                .username("jeb")
                .email("jenob@gmail.com")
                .password("12345")
                .location("kochi")
                .Description("Hi this is test purpose")
                .mobileNumber("9840280239")
                .role(UserRole.SUPER_ADMIN)
                .build();

        // Mock the AuthService to return the test user when getById is called
        Optional<Test_User> user=testRepo.findById(1);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/client/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("jenob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("jeb"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jenob@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location").value("kochi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Hi this is test purpose"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value("9840280239"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("SUPER_ADMIN"));
    }
}