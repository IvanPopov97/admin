package ru.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.blockhound.BlockHound;


@SpringBootTest
class AdminApplicationTests {


	AdminApplicationTests() {
	}

	@Test
	void contextLoads() {
		BlockHound.install();
	}

	@Test
	void addUserTest() {
	}
}
