package ru.danilakondr.testsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.*;
import ru.danilakondr.testsystem.dao.AnswerVariantDAO;
import ru.danilakondr.testsystem.dao.QuestionDAO;
import ru.danilakondr.testsystem.dao.TestDAO;
import ru.danilakondr.testsystem.dao.UserDAO;
import ru.danilakondr.testsystem.data.*;
import ru.danilakondr.testsystem.protocol.*;
import ru.danilakondr.testsystem.services.TestService;
import ru.danilakondr.testsystem.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
		"testsystem.admin.init.email=admin@example.org",
		"testsystem.admin.init.password=Secret",
		"spring.datasource.url=jdbc:h2:mem:testsystem",
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TestsystemApplicationTests {
	@RegisterExtension
	static GreenMailExtension greenMail = new GreenMailExtension(ServerSetup.SMTP)
			.withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "password"))
			.withPerMethodLifecycle(false);

	static final String API_REGISTER = "/api/register";
	static final String API_LOGIN = "/api/login";
	static final String API_LOGOUT = "/api/logout";

	static final String R_REGISTER = "{\"login\": \"test1\", \"email\": \"test1@example.org\", " +
			"\"password\": \"12345678\"}";
	static final String R_LOGIN = "{\"login\": \"test\", \"password\": \"12345678\"}";
	static final String R_ADMIN_LOGIN = "{\"login\": \"admin\", \"password\": \"Secret\"}";

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private TestDAO testDAO;

	@Autowired
	private QuestionDAO questionDAO;

	@Autowired
	private AnswerVariantDAO answerVariantDAO;

	@Autowired
	@Lazy
	private PasswordEncoder encoder;

	@Autowired
	private UserService userService;

	@Autowired
	private TestService testService;

	@Test
	void testRegister() throws Exception {
		mvc.perform(post(API_REGISTER).contentType(APPLICATION_JSON).content(R_REGISTER))
				.andExpect(status().isNoContent());
		Optional<User> user = userService.find("test1");
		assertTrue(user.isPresent());
		assertEquals("test1@example.org", user.get().getEmail());
		assertTrue(encoder.matches("12345678", user.get().getPassword()));
	}

	@Test
	void testCreateTest() throws Exception {
		userService.register("test2", "test2@example.org", "12345678");
		UserSession session = userService.login("test2", "12345678");
		String token = "Bearer " + session.getId().toString();

		TestBody body = new TestBody();
		body.setName("First tset");
		MvcResult result = mvc.perform(put("/api/test")
						.contentType(APPLICATION_JSON).content(mapper.writeValueAsString(body))
						.header("Authorization", token)
						.header("Host", "localhost"))
				.andExpect(status().isCreated())
				.andReturn();

		String[] segments = Objects.requireNonNull(result.getResponse().getHeader("Location")).split("/");
		String idString = segments[segments.length - 1];
		long id = Long.parseLong(idString);

		Optional<ru.danilakondr.testsystem.data.Test> test = testDAO.findById(id);
		assertTrue(test.isPresent());
		assertEquals("First tset", test.get().getName());
	}

	@Test
	void testCreateQuestion() throws Exception {
		userService.register("test3", "test3@example.org", "12345678");
		UserSession session = userService.login("test3", "12345678");
		User user = session.getUser();
		String token = "Bearer " + session.getId().toString();

		ru.danilakondr.testsystem.data.Test test = testService.create(user, "First test");
		QuestionBody request = new QuestionBody();
		request.setTestId(test.getId());
		request.setText("Question #1");
		List<Description.AnswerVariant> variants = new ArrayList<>();
		variants.add(new Description.AnswerVariant(0, "Answer 1", true));
		variants.add(new Description.AnswerVariant(0, "Answer 2", true));
		variants.add(new Description.AnswerVariant(0, "Answer 3", true));
		variants.add(new Description.AnswerVariant(0, "Answer invalid", false));
		request.setVariants(variants);
		request.setType(Question.Type.STRING_ENTRY);

		String content = mapper.writeValueAsString(request);
		MvcResult result = mvc.perform(put("/api/question")
					.header("Authorization", token)
						.header("Host", "localhost")
					.contentType(APPLICATION_JSON)
					.content(content))
				.andExpect(status().isCreated())
				.andReturn();

		String[] segments = Objects.requireNonNull(result.getResponse().getHeader("Location")).split("/");
		String idString = segments[segments.length - 1];
		long id = Long.parseLong(idString);

		Optional<Question> question = questionDAO.findById(id);
		assertTrue(question.isPresent());
		assertEquals("Question #1", question.get().getText());
	}
}
