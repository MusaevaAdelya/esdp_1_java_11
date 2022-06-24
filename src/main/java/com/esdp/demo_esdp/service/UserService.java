package com.esdp.demo_esdp.service;

import com.esdp.demo_esdp.dto.ProductDTO;
import com.esdp.demo_esdp.dto.UserResponseDTO;
import com.esdp.demo_esdp.dto.UserUpdateForm;
import com.esdp.demo_esdp.entity.User;
import com.esdp.demo_esdp.enums.ProductStatus;
import com.esdp.demo_esdp.exception.UserAlreadyRegisteredException;
import com.esdp.demo_esdp.exception.UserNotFoundException;
import com.esdp.demo_esdp.dto.UserRegisterForm;
import com.esdp.demo_esdp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserResponseDTO register(UserRegisterForm form) {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new UserAlreadyRegisteredException();
        }

        var user = User.builder()
                .email(form.getEmail())
                .name(form.getName())
                .lastname(form.getLastName())
                .telNumber(form.getTelNumber())
                .login(form.getLogin())
                .password(encoder.encode(form.getPassword()))
                .build();

        userRepository.save(user);

        return UserResponseDTO.from(user);
    }

    public UserResponseDTO update(UserUpdateForm form) {
        if (!userRepository.existsById(form.getId())) {
            throw new UserNotFoundException();
        }


        userRepository.updateUserData(form.getName(), form.getLastName(), form.getEmail(), form.getTelNumber(), form.getLogin(), form.getId());

        var user = userRepository.findById(form.getId()).get();

        return UserResponseDTO.from(user);
    }

    public UserResponseDTO getByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return UserResponseDTO.from(user);
    }

    public List<UserResponseDTO> getUsers() {
        return userRepository.getUsers("Admin")
                .stream().map(UserResponseDTO::from).collect(Collectors.toList());
    }

    public void blockingUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (user.isEnabled()) {
            userRepository.updateEnabledUser(false, user.getId());
        } else {
            userRepository.updateEnabledUser(true, user.getId());
        }
    }
}
