package ru.voltjunkie.authenticationservice.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import ru.voltjunkie.authenticationservice.configs.RsaProperties;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Configuration
// a lot to-do
public class JwtUtil {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private int expiration;
    private int refreshExpirationMultiplier;
    private final RsaProperties rsaProperties;
    private final Algorithm algorithm;

    JwtUtil(RsaProperties rsaProperties) {
        this.rsaProperties = rsaProperties;
        loadKeys();
        algorithm = Algorithm.RSA256(publicKey, privateKey);
    }

    public String generateToken(Long userId, String role, String tokenType) {
        return JWT.create()
                .withSubject(Long.toString(userId))
                .withIssuer("voltjunkie.ru")
                .withExpiresAt(new Date(new Date().getTime() +
                        (tokenType.equalsIgnoreCase("ACCESS") ? (long) expiration * 1000 : (long) expiration * 1000 * refreshExpirationMultiplier)))
                .withIssuedAt(new Date())
                .withClaim("role", role)
                .withClaim("tokenType", tokenType)
                .sign(algorithm);
    }

    public Map<String, Claim> getAllClaims(String token) {
        token = token.replace("Bearer ", "");
        return JWT.decode(token).getClaims();
    }

    public String getPublicKey() {
        System.out.println();
        return rsaProperties.getPublicKey();
    }

    public Boolean verifyToken(String token) {
        token = token.replace("Bearer ", "");
        final Verification verification = JWT.require(algorithm);
        final JWTVerifier verifier = verification.build();
        verifier.verify(token);
        return true;
    }

    private void loadKeys() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            X509EncodedKeySpec publicKeySpec =
                    new X509EncodedKeySpec(Base64.getDecoder().decode(rsaProperties.getPublicKey()));
            publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

            PKCS8EncodedKeySpec keySpec =
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaProperties.getPrivateKey()));
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

            expiration = rsaProperties.getExpiration();
            refreshExpirationMultiplier = rsaProperties.getRefreshExpirationMultiplier();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


}
