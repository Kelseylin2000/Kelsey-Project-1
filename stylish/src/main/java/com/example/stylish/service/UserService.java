package com.example.stylish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.stylish.dto.AuthResponseDto;
import com.example.stylish.dto.DataResponseDto;
import com.example.stylish.dto.FacebookUser;
import com.example.stylish.dto.SignInRequestDto;
import com.example.stylish.dto.SignUpRequestDto;
import com.example.stylish.dto.UserDto;
import com.example.stylish.exception.BadRequestException;
import com.example.stylish.exception.EmailAlreadyExistsException;
import com.example.stylish.exception.SignInFailException;
import com.example.stylish.repository.UserRepository;
import com.example.stylish.security.JwtUtil;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Value("${facebook.graph.api.url}")
    private String facebookGraphApiUrl;

    @Value("${jwt.expiration}")
    private Long expiration;

    private static String defaultRole = "user";


    public DataResponseDto<AuthResponseDto> signUp(SignUpRequestDto signUpRequest){

        if(userRepository.emailExists(signUpRequest.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        userRepository.saveUser("native", signUpRequest.getName(),  signUpRequest.getEmail(), encodedPassword, "", null);
        UserDto user = userRepository.findUserByEmail(signUpRequest.getEmail());
        userRepository.saveUserRole(user.getId(), defaultRole);
        List<String> roles = userRepository.findRoleByUserId(user.getId());
        String token = jwtUtil.generateToken(user, roles);
        AuthResponseDto response = new AuthResponseDto(token, expiration, user);

        return new DataResponseDto<>(response);
    }

    public DataResponseDto<AuthResponseDto> signIn(SignInRequestDto signInRequest){
        DataResponseDto<AuthResponseDto> response;

        if ("native".equalsIgnoreCase(signInRequest.getProvider())) {
            response = this.signInNative(signInRequest.getEmail(), signInRequest.getPassword());
        } else if ("facebook".equalsIgnoreCase(signInRequest.getProvider())) {
            response = this.signInFacebook(signInRequest.getAccessToken());
        } else {
            throw new BadRequestException("Unsupported provider");
        }

        return response;
    }

    private DataResponseDto<AuthResponseDto> signInNative(String email, String password){

        if(!userRepository.emailExists(email)){
            throw new SignInFailException("Invalid email");
        }

        UserDto user = userRepository.findUserByEmail(email);
        String storedPassword = userRepository.findPasswordByEmail(email);
        
        if (!passwordEncoder.matches(password, storedPassword)) {
            throw new SignInFailException("Invalid password");
        } 

        List<String> roles = userRepository.findRoleByUserId(user.getId());
        String token = jwtUtil.generateToken(user, roles);
        AuthResponseDto response = new AuthResponseDto(token, expiration, user);

        return new DataResponseDto<>(response);
    }

    private DataResponseDto<AuthResponseDto> signInFacebook(String accessToken){
        String url = facebookGraphApiUrl + "?access_token=" + accessToken + "&fields=id,name,email,picture";
        RestTemplate restTemplate = new RestTemplate();
        FacebookUser fbUser;

        try{
            fbUser = restTemplate.getForObject(url, FacebookUser.class);
        }catch(HttpClientErrorException ex){
            throw new BadRequestException("Invalid Facebook access token");
        }

        if (fbUser == null || fbUser.getId() == null) {
            throw new BadRequestException("Invalid Facebook access token");
        }

        UserDto user = findOrCreateUser(fbUser);
        List<String> roles = userRepository.findRoleByUserId(user.getId());
        String token = jwtUtil.generateToken(user, roles);
        AuthResponseDto response = new AuthResponseDto(token, expiration, user);

        return new DataResponseDto<>(response);
    }

    private UserDto findOrCreateUser(FacebookUser fbUser) {
        if (userRepository.facebookIdExists(fbUser.getId())) {
            UserDto user = userRepository.findUserByFacebookId(fbUser.getId());
            userRepository.updateUser(fbUser.getName(), fbUser.getPicture().getData().getUrl(), fbUser.getEmail(), user.getId());
        } else {
            int userId = userRepository.saveUser("facebook", fbUser.getName(),fbUser.getEmail(), null, fbUser.getPicture().getData().getUrl(), fbUser.getId());
            userRepository.saveUserRole(userId, defaultRole);
        }
        return userRepository.findUserByFacebookId(fbUser.getId());
    }


    public DataResponseDto<UserDto> getUserProfile(String token){

        String jwt = token.substring(7);
        UserDto user = jwtUtil.getUserDtoFromToken(jwt);
        return new DataResponseDto<>(user);
    }
}
