package ru.admin;

import io.r2dbc.postgresql.util.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.blockhound.BlockHound;
import ru.admin.service.EmailService;
import ru.admin.service.UserService;

import java.util.Map;

@SpringBootTest
class AdminApplicationTests {

	public static final int CONFIRMATION_TOKEN_LIFETIME = 15;

	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;

	AdminApplicationTests() {
	}

	@Test
	void contextLoads() {
		BlockHound.install();
	}

	@Test
	void userServiceTest() {
		Assert.isTrue(userService.existsByEmail("golden.royal@mail.ru").blockOptional().orElse(false), "Пользователь golden.royal@mail.ru не найден");
	}

	@Test
	void emailServiceTest() {
		emailService.sendTemplate("golden.royal@mail.ru", "Регистрация", "account-activation.ftl",
				Map.of("activationLink", "http://localhost:8080", "timeLimitInMinutes", CONFIRMATION_TOKEN_LIFETIME));
	}
}
