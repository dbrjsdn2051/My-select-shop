package org.example.myselectshop.service;

import lombok.RequiredArgsConstructor;
import org.example.myselectshop.dto.SignupRequestDto;
import org.example.myselectshop.entity.User;
import org.example.myselectshop.entity.UserRoleEnum;
import org.example.myselectshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto){
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);

        if(checkUsername.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);

        if(checkEmail.isPresent()){
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;

        if(requestDto.isAdmin()){
            if(!ADMIN_TOKEN.equals(requestDto.getAdminToken())){
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
        }

        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

}
