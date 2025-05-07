package mate.academy.bookshop.service;

import mate.academy.bookshop.dto.user.UserRegistrationRequestDto;
import mate.academy.bookshop.dto.user.UserResponseDto;
import mate.academy.bookshop.exceptions.RegistrationException;

public interface UserService {
    UserResponseDto register(String email, UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
