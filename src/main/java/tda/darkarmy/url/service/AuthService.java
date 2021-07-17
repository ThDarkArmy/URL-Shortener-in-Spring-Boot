package tda.darkarmy.url.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tda.darkarmy.url.dto.*;
import tda.darkarmy.url.exception.DarkException;
import tda.darkarmy.url.exception.UserExistsException;
import tda.darkarmy.url.exception.UserNotFoundException;
import tda.darkarmy.url.mapper.UserMapper;
import tda.darkarmy.url.model.ResetPassword;
import tda.darkarmy.url.model.User;
import tda.darkarmy.url.model.VerificationToken;
import tda.darkarmy.url.repository.ResetPasswordRepository;
import tda.darkarmy.url.repository.UserRepository;
import tda.darkarmy.url.repository.VerificationTokenRepository;
import tda.darkarmy.url.security.Constants;
import tda.darkarmy.url.security.TokenProvider;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private TokenProvider tokenProvider;
    private RefreshTokenService refreshTokenService;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;
    private VerificationTokenRepository verificationTokenRepository;
    private ResetPasswordRepository resetPasswordRepository;

    @Transactional
    public GeneralApiResponse register(UserDto userDto) throws UserExistsException, DarkException {
        LOG.info("Roles: "+userDto.getRoles().toString());
        User user = userRepository.findByEmail(userDto.getEmail());
        if(user!=null){
            throw  new UserExistsException("User with given email already exists.");
        }
        user = userMapper.map(userDto);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new VerificationEmail("Activate Your account!",user.getEmail(),
                "Thank you for signing up to Url shortener, " +
                        "please click on the below url to activate your account : ",
                "http://localhost:8000/api/v1/auth/accountVerification/" + token));
        return GeneralApiResponse.builder().statusCode(201).message("User registration successful, Please check your mail to verify your account.").build();
    }

    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public void verifyAccount(String token) throws DarkException, UserNotFoundException {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken==null) throw new DarkException("Invalid Token!");
        fetchUserAndEnable(verificationToken);
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) throws UserNotFoundException {
        String username = verificationToken.getUser().getEmail();
        User user = userRepository.findByEmail(username);

        if(user==null) throw new UserNotFoundException("User with token does not exists.");
        user.setActive(true);
        userRepository.save(user);
    }


    public AuthenticationResponse login(LoginRequest request) throws UserNotFoundException {
        User user = userRepository.findByEmail(request.getUsername());

        if(user == null){
            throw new UserNotFoundException("User with given email does not exists.");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(Constants.EXPIRATION_TIME))
                .username(request.getUsername())
                .build();
    }

    public UserDto findByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null) throw new UserNotFoundException("User with given email does not exists.");

        return userMapper.mapToDto(user);
    }

    @Transactional
    public GeneralApiResponse forgotPassword(ResetPasswordRequest resetPasswordRequest) throws UserNotFoundException, DarkException {

        User user = userRepository.findByEmail(resetPasswordRequest.getEmail());
        if(user == null) throw new UserNotFoundException("User with given email "+ resetPasswordRequest.getEmail()+ " does not exists.");
        String token = generateVerificationToken(user);
        ResetPassword resetPassword = ResetPassword.builder()
                .email(resetPasswordRequest.getEmail())
                .password(resetPasswordRequest.getPassword())
                .verificationToken(token)
                .build();
        resetPasswordRepository.save(resetPassword);
        mailService.sendMail(new VerificationEmail("Reset password!",user.getEmail(),
                "Thank you for being with us, " +
                        "please click on the below url to confirm reset password : ",
                "http://localhost:8000/api/v1/auth/resetPasswordVerification/" + token));

        return GeneralApiResponse.builder().statusCode(201).message("Check your email to confirm reset password.").build();
    }

    @Transactional
    public GeneralApiResponse resetPassword(String token) throws DarkException, UserNotFoundException {
        ResetPassword rstPassword = resetPasswordRepository.findByVerificationToken(token);

        if(rstPassword==null) throw new DarkException("Invalid Token!");
        User user = userRepository.findByEmail(rstPassword.getEmail());
        if(user == null) throw new UserNotFoundException("User with given email does not exists.");
        user.setPassword(passwordEncoder.encode(rstPassword.getPassword()));
        return GeneralApiResponse.builder().statusCode(201).message("Password reset successful.").build();
    }



    @Transactional(readOnly = true)
    public User getCurrentUser() throws UserNotFoundException {

        LOG.info("current User: "+ SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userRepository.findByEmail(username);
        if(user == null) throw new UserNotFoundException("User not found.");
        return user;

    }

    public boolean isLoggedIn() {
        return true;
    }

//    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws SpringRedditException {
//        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
//        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
//        return AuthenticationResponse.builder()
//                .refreshToken(refreshTokenRequest.getRefreshToken())
//                .username(refreshTokenRequest.getUsername())
//                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
//                .authenticationToken(token)
//                .build();
//    }

}
