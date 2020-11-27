package com.example.rest.dvdrental.v2;

import com.example.rest.dvdrental.v2.exceptions.InvalidJwtTokenException;
import com.example.rest.dvdrental.v2.utils.JwtUtil;
import lombok.extern.java.Log;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log
class DvdrentalV2ApplicationTests {
	
	@Autowired
	private JwtUtil jwtUtil;

	@Test
	void contextLoads() {
	}
	
	@Test
	@DisplayName("Verify if token has the correct info")
	void verifyToken() {
		String token = jwtUtil.generateToken("admin1");
		log.info("Nimbus token: " + token);
		jwtUtil.validateToken(token);
		assertEquals("admin1", jwtUtil.extractUsername(token));
	}
	
	@Test
	@DisplayName("Validation fail with an unknown token")
	void tokenValidationFail() {
		assertThrows(InvalidJwtTokenException.class, () -> jwtUtil.validateToken("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.VFb0qJ1LRg_4ujbZoRMXnVkUgiuKq5KxWqNdbKq_G9Vvz-S1zZa9LPxtHWKa64zDl2ofkT8F6jBt_K4riU-fPg"));
	}
}
