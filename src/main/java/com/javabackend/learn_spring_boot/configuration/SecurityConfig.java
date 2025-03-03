package com.javabackend.learn_spring_boot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {"/api/users",
            "/api/auth/log-in", "/api/auth/introspect", "/api/auth/logout"
    };

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Value("${env.SIGNER_KEY}")
    protected String SIGNER_KEY;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // nhung request cho phep ma khong can token
        httpSecurity.authorizeHttpRequests(request ->
                // cho phep request ma khong can JWT
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        // chi admin moi duoc truy cap
                        //phan quyen tren End Point nhu nay it pho bien nen chuyen qua dung tren method
                        //.requestMatchers(HttpMethod.GET,"/api/users").hasAuthority("ROLE_ADMIN")
                        // cac request khac phai co jwt hop le
                        .anyRequest().authenticated());

        // cai dat xac thuc token cho cac request
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        // tat csrf cua spring security khi config
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }



    // converter(bo chuyen doi), tu dong gan them ROLE_ vao scope
    // Vi khi dung OAuth2 voi JWT, Spring Security mac dinh tim quyen trong scope.
    // Neu tu xay dung JWT (khong dung OAuth2), ta co the dung role hoac customgi bat ki claim nao ma ko can converter lai
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); //vi da them ROLE vao o ben Service cho Scope nen ko can nua

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
