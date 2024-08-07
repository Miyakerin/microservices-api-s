package ru.voltjunkie.userservice.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtUtil {

    private final RestTemplate restTemplate;
    private RSAPublicKey publicKey;
    private Algorithm algorithm;

    public Map<String, Claim> getAllClaims(String token) {
        token = token.replace("Bearer ", "");
        return JWT.decode(token).getClaims();
    }

    public Boolean validateToken(String token) {
        loadKeys();
        token = token.replace("Bearer ", "");
        final Verification verification = JWT.require(algorithm).withClaim("tokenType", "ACCESS");
        final JWTVerifier verifier = verification.build();
        verifier.verify(token);
        return true;
    }

    private void loadKeys() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                    Base64.getDecoder().decode(
                            restTemplate.getForObject("http://auth-service/api/auth/jwt/publicKey", String.class)
                    )
            );
            publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
            algorithm = Algorithm.RSA256(publicKey);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
