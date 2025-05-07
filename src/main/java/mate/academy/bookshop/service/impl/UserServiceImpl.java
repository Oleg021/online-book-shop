package mate.academy.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.user.UserRegistrationRequestDto;
import mate.academy.bookshop.dto.user.UserResponseDto;
import mate.academy.bookshop.exceptions.RegistrationException;
import mate.academy.bookshop.mapper.UserMapper;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.repository.user.UserRepository;
import mate.academy.bookshop.service.UserService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto register(String email, UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setEmail(email);
        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
    }
}
