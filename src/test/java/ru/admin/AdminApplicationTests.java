package ru.admin;

import io.r2dbc.postgresql.util.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.admin.service.UserService;

@SpringBootTest
class AdminApplicationTests {

	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
		// BlockHound.install();
	}

	@Test
	void userServiceTest() {
		Assert.isTrue(userService.existsByEmail("golden.royal@mail.ru").blockOptional().orElse(false), "Пользователь golden.royal@mail.ru не найден");
	}
}
