package br.ufes.ccens.util;

import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import br.ufes.ccens.entity.StudentEntity;
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
    public String generateToken(StudentEntity student) {
        String token = Jwt.issuer(issuer) 
                .upn(student.getEmail()) 
                .groups(new HashSet<>(Arrays.asList("User"))) 
                .claim(Claims.birthdate.name(), "2001-07-13") 
                .sign();

        System.out.println(token);
        return token;
    }
}