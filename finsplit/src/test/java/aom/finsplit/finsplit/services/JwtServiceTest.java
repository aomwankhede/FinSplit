package aom.finsplit.finsplit.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testCreateAndVerifyToken_Success() {
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", "123");
        claims.put("email", "aom@example.com");

        String token = jwtService.createToken(claims);
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3, "JWT should have 3 parts");

        Optional<Map<String, String>> result = jwtService.verifyAndGet(token);
        assertTrue(result.isPresent());

        Map<String, String> verifiedClaims = result.get();
        assertEquals("123", verifiedClaims.get("userId"));
        assertEquals("aom@example.com", verifiedClaims.get("email"));
    }

    @Test
    void testVerifyToken_InvalidSignature() {
        String invalidToken = "abc.def.ghi";
        Optional<Map<String, String>> result = jwtService.verifyAndGet(invalidToken);
        assertTrue(result.isEmpty());
    }

    @Test
    void testVerifyToken_TamperedPayload() {
        Map<String, String> claims = new HashMap<>();
        claims.put("role", "admin");

        String token = jwtService.createToken(claims);

        // Tamper with payload
        String[] parts = token.split("\\.");
        String tamperedPayload = parts[1].substring(0, parts[1].length() - 1) + "x";
        String tamperedToken = parts[0] + "." + tamperedPayload + "." + parts[2];

        Optional<Map<String, String>> result = jwtService.verifyAndGet(tamperedToken);
        assertTrue(result.isEmpty());
    }

    @Test
    void testVerifyToken_InvalidFormat() {
        Optional<Map<String, String>> result = jwtService.verifyAndGet("not.a.jwt");
        assertTrue(result.isEmpty());
    }
}
