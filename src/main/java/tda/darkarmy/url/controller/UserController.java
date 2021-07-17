package tda.darkarmy.url.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tda.darkarmy.url.dto.GeneralApiResponse;
import tda.darkarmy.url.dto.UserDto;
import tda.darkarmy.url.exception.DarkException;
import tda.darkarmy.url.exception.UserNotFoundException;
import tda.darkarmy.url.service.UserService;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RequestMapping("/api/v1/user")
@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/byId/{id}")
    public ResponseEntity<?> findUserById(@PathVariable("id") Long id) throws UserNotFoundException {
        UserDto user = userService.findById(id);
        return status(200).body(user);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        List<UserDto> users = userService.findAll();
        return status(200).body(users);
    }

    @PutMapping("/")
    public ResponseEntity<?> update(@Valid @RequestBody Map<String, String> nameMap) throws UserNotFoundException {
        GeneralApiResponse response = userService.updateUser(nameMap.get("name"));
        return status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> delete() throws UserNotFoundException {
        GeneralApiResponse response = userService.deleteUser();
        return status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/byId/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) throws UserNotFoundException {
        GeneralApiResponse response = userService.deleteUserById(id);
        return status(HttpStatus.OK).body(response);
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@RequestBody Map<String, String> emailMap) throws UserNotFoundException, DarkException {
        GeneralApiResponse response = userService.changeEmail(emailMap.get("email"));
        return status(201).body(response);
    }

    @GetMapping("/changeEmailVerification/{token}")
    public ResponseEntity<?> changeEmailVerification(@PathVariable("token") String token) throws UserNotFoundException, DarkException {
        GeneralApiResponse response = userService.changeEmailVerification(token);
        return status(201).body(response);
    }
}

