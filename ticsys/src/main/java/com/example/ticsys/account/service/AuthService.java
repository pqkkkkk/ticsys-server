package com.example.ticsys.account.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.ticsys.account.dao.IUserDao;
import com.example.ticsys.account.dto.request.SignInRequest;
import com.example.ticsys.account.dto.response.SignInRepsonse;
import com.example.ticsys.account.model.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {
    private String SIGNER_KEY = "1TjXchw5FloESb63Kc+DFhTARvpWL4jUGCwfGWxuG5SIf/1y/LgJxHnMqaF6A/ij";
    private final IUserDao userDao;
    private PasswordEncoder passwordEncoder;
    public AuthService (IUserDao userDao, PasswordEncoder passwordEncoder)
    {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }
    public SignInRepsonse signin(SignInRequest signInRequest)
    {
        String username = signInRequest.getUserName();
        String password = signInRequest.getPassWord();
        User user = userDao.GetUserByUsername(username);

        if(user == null)
        {
            return  SignInRepsonse.builder()
                        .authenticated(false)
                        .message("user is not exist")
                        .token("")
                        .user(null)
                        .build();
        }

        boolean authenticated = passwordEncoder.matches(password, user.getPassWord());

        if(!authenticated)
        {
            return  SignInRepsonse.builder()
                        .authenticated(false)
                        .message("Wrong password")
                        .token("")
                        .build(); 
        }

        return  SignInRepsonse.builder()
                    .authenticated(true)
                    .message("Login successfully")
                    .token(generateToken(user))
                    .user(user)
                    .build();
    }
    private String generateToken(User user)
    {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                                .subject(user.getUserName())
                                .issuer("pqkiet854")
                                .issueTime(new Date())
                                .expirationTime(new Date(
                                                Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                                                ))
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
    private String buildScope(User user)
    {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);

        return stringJoiner.toString();
    }
}
