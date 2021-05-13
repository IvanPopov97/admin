package ru.admin;

import io.r2dbc.postgresql.util.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.blockhound.BlockHound;
import ru.admin.enitity.ConfirmationToken;
import ru.admin.enitity.User;
import ru.admin.repository.ConfirmationTokenRepository;
import ru.admin.repository.UserRepository;
import ru.admin.service.ConfirmationTokenService;
import ru.admin.service.EmailService;
import ru.admin.service.UserService;

import java.util.Map;
import java.util.Objects;

@SpringBootTest
class AdminApplicationTests {

	public static final int CONFIRMATION_TOKEN_LIFETIME = 15;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private AmqpTemplate messagingTemplate;
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	@Autowired
	private ConfirmationTokenService confirmationTokenService;

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

	@Test
	void senderTest() {
		messagingTemplate.convertAndSend("account-activation", "golden.royal@mail.ru");
	}

	@Test
	void setConfirmationTokenRepositoryTest() {
		Assert.isTrue(Objects.requireNonNull(confirmationTokenRepository.findByCode("2134").block()).getCode().equals("2134"), "Что-то пошло не так");
		confirmationTokenRepository.save(new ConfirmationToken()).block();
	}

	@Test
	void setConfirmationTokenServiceTest() {
		confirmationTokenService.createForUser(userRepository.findByEmail("golden.royal@mail.ru")).subscribe();
	}
}
