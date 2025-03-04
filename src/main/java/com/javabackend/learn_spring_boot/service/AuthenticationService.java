package com.javabackend.learn_spring_boot.service;

import com.javabackend.learn_spring_boot.dto.request.AuthenticationRequest;
import com.javabackend.learn_spring_boot.dto.request.IntrospectRequest;
import com.javabackend.learn_spring_boot.dto.request.LogoutRequest;
import com.javabackend.learn_spring_boot.dto.request.RefeshRequest;
import com.javabackend.learn_spring_boot.dto.response.AuthenticationResponse;
import com.javabackend.learn_spring_boot.dto.response.IntrospectResponse;
import com.javabackend.learn_spring_boot.exception.AppException;
import com.javabackend.learn_spring_boot.exception.ErrorCode;
import com.javabackend.learn_spring_boot.model.InvalidatedToken;
import com.javabackend.learn_spring_boot.model.User;
import com.javabackend.learn_spring_boot.repository.InvalidatedTokenRepository;
import com.javabackend.learn_spring_boot.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${env.SIGNER_KEY}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${env.VALID_DURATION}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${env.REFRESHABLE_DURATION}")
    protected long REFRESHABLE_DURATION;

    // kiem tra user va password
    public AuthenticationResponse authenticate (AuthenticationRequest request){
        var user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(),user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authentication(true)
                .build();
    }

    // tao token
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("learnjava.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.MINUTES).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    // tao scope de cho vao token
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role ->{
                stringJoiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
            });

        return stringJoiner.toString();
    }

    // ham nay luc dau nam o trong introspect
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.MINUTES).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    // kiem tra token
    public IntrospectResponse introspect(IntrospectRequest request)  throws JOSEException, ParseException {
        var token = request.getToken();
        /*
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();*/
        boolean isValid = true;

        try { // kiem tra code co the gay ra loi
            verifyToken(token,false); // kiem tra token con han khong, hoac co bi logout chua
        } catch (AppException e) { // xu ly loi do
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    //log out token
    public void logout(LogoutRequest request) throws ParseException, JOSEException{
        try {
            // vi co the het thoi gian su dung, nhung con thoi gian refresh
            var signToken = verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);

        }catch (AppException appException){
            log.info("Token already expired");
        }

    }

    //refresh token
    public AuthenticationResponse refreshToken(RefeshRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(),true);
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
        var userName = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authentication(true)
                .build();
    }
}
