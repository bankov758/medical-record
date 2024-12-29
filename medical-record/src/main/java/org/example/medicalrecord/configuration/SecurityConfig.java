package org.example.medicalrecord.configuration;

//import com.nimbusds.jose.jwk.JWK;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;

import lombok.AllArgsConstructor;
import org.example.medicalrecord.data.enums.Roles;
import org.example.medicalrecord.service.UserService;
import org.example.medicalrecord.util.RSAKeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final RSAKeyProperties keys;

    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    //    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/auth/**").permitAll();
//                    auth.requestMatchers("/headmasters/**").hasRole(Roles.ADMIN.name());
//                    auth.requestMatchers("/grades/**").hasRole(Roles.ADMIN.name());
//                    //auth.requestMatchers("/user/**").hasAnyRole("ADMIN", "USER");
//                    auth.anyRequest().authenticated();
//                })
//                .logout(logout -> logout
//                        .logoutUrl("/auth/logout")
//                        .logoutSuccessUrl("/auth/login")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                )
////                .oauth2ResourceServer(oauth2 -> oauth2
////                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                //                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults())
//                .build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz.requestMatchers("/recipes/**")
                        .hasAuthority("DOCTOR").requestMatchers("/medicines/**")
                        .hasAuthority("SELLER").requestMatchers(HttpMethod.GET, "/medicines")
                        .hasAuthority("CUSTOMER").anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }


//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter(){
//        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
//        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
//        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
//        return jwtConverter;
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder(){
//        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
//    }
//
//    @Bean
//    public JwtEncoder jwtEncoder(){
//        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
//        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
//        return new NimbusJwtEncoder(jwks);
//    }

}
