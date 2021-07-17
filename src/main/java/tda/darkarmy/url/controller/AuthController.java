package tda.darkarmy.url.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tda.darkarmy.url.dto.*;
import tda.darkarmy.url.exception.DarkException;
import tda.darkarmy.url.exception.UserExistsException;
import tda.darkarmy.url.exception.UserNotFoundException;
import tda.darkarmy.url.service.AuthService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) throws UserExistsException, DarkException {
        GeneralApiResponse response = authService.register(userDto);

        return status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable("token") String token) throws UserNotFoundException, DarkException {
        authService.verifyAccount(token);
        return status(HttpStatus.OK).body("Account verified.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) throws UserNotFoundException {
        AuthenticationResponse authenticationResponse = authService.login(loginRequest);
        return status(HttpStatus.OK).body(authenticationResponse);
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable("email") String email) throws UserNotFoundException {
        UserDto userDto = authService.findByEmail(email);
        return status(HttpStatus.OK).body(userDto);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws UserNotFoundException, DarkException {
        GeneralApiResponse response = authService.forgotPassword(resetPasswordRequest);
        return status(HttpStatus.OK).body(response);
    }

    @GetMapping("/resetPasswordVerification/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token) throws UserNotFoundException, DarkException {
        GeneralApiResponse response = authService.resetPassword(token);
        return status(HttpStatus.OK).body(response);
    }

}
