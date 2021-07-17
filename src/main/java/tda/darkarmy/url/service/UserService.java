package tda.darkarmy.url.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tda.darkarmy.url.dto.GeneralApiResponse;
import tda.darkarmy.url.dto.UserDto;
import tda.darkarmy.url.dto.VerificationEmail;
import tda.darkarmy.url.exception.DarkException;
import tda.darkarmy.url.exception.UserNotFoundException;
import tda.darkarmy.url.mapper.UserMapper;
import tda.darkarmy.url.model.ChangeEmail;

import tda.darkarmy.url.model.User;
import tda.darkarmy.url.repository.ChangeEmailRepository;
import tda.darkarmy.url.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private AuthService authService;
    private UserMapper userMapper;
    private ChangeEmailRepository changeEmailRepository;
    private MailService mailService;

    public UserDto findById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User with given id not found"));
        return userMapper.mapToDto(user);
    }

    public List<UserDto> findAll(){
        List<User> userList = userRepository.findAll();
        return userList.stream().map(user -> userMapper.mapToDto(user)).collect(Collectors.toList());
    }

    public GeneralApiResponse updateUser(String name) throws UserNotFoundException {
        User user = authService.getCurrentUser();

        // log.info("User Name: "+user.getName());
        user.setName(name);
//        Set<Roles> roles = new HashSet<>();
//        for(String role: userDto.getRoles()){
//            Roles roles1 = new Roles();
//            roles1.setName(Role.valueOf(role));
//            roles.add(roles1);
//        }
//
//        user.setRoles(roles);
        userRepository.save(user);

        return GeneralApiResponse.builder().statusCode(200).message("User updated successfully.").build();
    }

    @Transactional
    public GeneralApiResponse deleteUser() throws UserNotFoundException {
        User user = authService.getCurrentUser();
        userRepository.deleteById(user.getId());

        return GeneralApiResponse.builder().statusCode(200).message("User updated successfully.").build();
    }

    @Transactional
    public GeneralApiResponse deleteUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User not found"));

        userRepository.deleteById(id);

        return GeneralApiResponse.builder().statusCode(200).message("User deleted successfully.").build();
    }

    @Transactional
    public GeneralApiResponse changeEmail(String email) throws UserNotFoundException, DarkException {
        User user = authService.getCurrentUser();
        String token = authService.generateVerificationToken(user);
        changeEmailRepository.save(ChangeEmail.builder().token(token).newEmail(email).build());
        mailService.sendMail(new VerificationEmail("Change Email!",user.getEmail(),
                "Thank you for signing up to Url shortener, " +
                        "please click on the below url to change your email : ",
                "http://localhost:8000/api/v1/auth/changeEmailVerification/" + token));

        return GeneralApiResponse.builder().statusCode(201).message("Check your mail to change your email.").build();
    }

    @Transactional
    public GeneralApiResponse changeEmailVerification(String token) throws DarkException, UserNotFoundException {
        ChangeEmail changeEmail = changeEmailRepository.findByToken(token);
        if(changeEmail == null) throw new DarkException("Invalid token!");
        User user = authService.getCurrentUser();
        user.setEmail(changeEmail.getNewEmail());
        userRepository.save(user);
        return GeneralApiResponse.builder().statusCode(201).message("Email changed successfully").build();
    }
}
