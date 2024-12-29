package org.example.medicalrecord.service.impl;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

//    private JwtEncoder jwtEncoder;
//
//    private JwtDecoder jwtDecoder;

    /**
     * Generates a JSON Web Token (JWT) from the provided {@link Authentication} object.
     * <p>
     * This method extracts the user’s principal (as the subject) and concatenates all granted
     * authorities into a single space-separated string under the claim "roles". The token is
     * issued by "self" and is encoded via a {@link JwtEncoder}.
     *
     * @param auth the {@link Authentication} object containing user principal and authorities
     * @return the generated JWT as a {@code String}
     */
    @Override
    public String generateJwt(Authentication auth){

//        Instant now = Instant.now();
//
//        String scope = auth.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(" "));
//
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("self")
//                .issuedAt(now)
//                .subject(auth.getName())
//                .claim("roles", scope)
//                .build();
//
//        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return null;
    }

}
