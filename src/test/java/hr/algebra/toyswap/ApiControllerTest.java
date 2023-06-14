package hr.algebra.toyswap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.toyswap.dto.AuthResponseDto;
import hr.algebra.toyswap.dto.LoginDto;
import hr.algebra.toyswap.dto.RegisterDto;
import hr.algebra.toyswap.model.user.User;
import hr.algebra.toyswap.model.user.UserRole;
import hr.algebra.toyswap.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class ApiControllerTest {

  private MockMvc mockMvc;

  @Autowired private WebApplicationContext webApplicationContext;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private UserRepository userRepository;

  private User testUser;

  @BeforeAll
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @AfterAll
  public void cleanup() {
    userRepository.delete(testUser);
  }

  @Test
  void shouldLogin() throws Exception {
    final var body = LoginDto.builder().email("admin@toyswap.com").password("pass").build();

    mockMvc
        .perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            result -> {
              final var response =
                  objectMapper.readValue(
                      result.getResponse().getContentAsString(), AuthResponseDto.class);
              assertEquals(response.getUser().getEmail(), body.getEmail());
              assertEquals("Admin", response.getUser().getFirstName());
              assertEquals(UserRole.ADMIN, response.getUser().getRole());
            });
  }

  @Test
  void shouldRegister() throws Exception {
    final var body =
        RegisterDto.builder()
            .email("test@test.com")
            .firstName("Test")
            .lastName("Testić")
            .phoneNumber("0919189777")
            .password("pass")
            .build();

    mockMvc
        .perform(
            post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("User registered successfully!"));

    testUser = userRepository.findByEmail(body.getEmail()).orElse(null);
  }
}
