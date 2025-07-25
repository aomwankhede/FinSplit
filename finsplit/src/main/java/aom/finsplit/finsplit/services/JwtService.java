package aom.finsplit.finsplit.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class JwtService {

    private static final ObjectMapper M = new ObjectMapper();
    private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder B64D = Base64.getUrlDecoder();

    private static final String HMAC_ALG = "HmacSHA256";

    private static final String SECRET_KEY = "AOMWANKHEDEISTHEGRETESTPERSONINTHISWORLD";

    public String createToken(Map<String, String> claims) {
        try {
            String header = toB64(json(Map.of("alg", "HS256", "typ", "JWT")));
            String payload = toB64(json(claims));
            String signature = sign(header + "." + payload);
            return header + "." + payload + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Map<String, String>> verifyAndGet(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3)
                return Optional.empty();

            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];

            if (!sign(header + "." + payload).equals(signature))
                return Optional.empty();

            Map<String, String> claims = fromJson(new String(B64D.decode(payload), StandardCharsets.UTF_8));
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static String json(Map<String, String> map) throws Exception {
        return M.writeValueAsString(map);
    }

    private static Map<String, String> fromJson(String json) throws Exception {
        return M.readValue(json, new TypeReference<Map<String, String>>() {
        });
    }

    private static String toB64(String s) {
        return B64.encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private static String sign(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_ALG);
        mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_ALG));
        return B64.encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}
