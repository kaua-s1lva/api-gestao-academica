package br.ufes.ccens.common.util;

import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.ufes.ccens.data.entity.UserEntity;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * A utility class to generate and print a JWT token string to stdout.
 */
@ApplicationScoped
public class GenerateToken {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    /**
     * Generates and prints a JWT token.
     */
    public String generateToken(UserEntity user) {
        String token = Jwt.issuer(issuer)
                .subject(user.getUserId().toString())
                .upn(user.getEmail()) 
                .groups(new HashSet<>(Arrays.asList(user.getRole().name())))
                .sign();

        System.out.println(token);
        return token;
    }
}