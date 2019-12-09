package com.havryliv.auction.service.impl;

import com.havryliv.auction.constants.ExceptionMessages;
import com.havryliv.auction.converter.UserConverter;
import com.havryliv.auction.dto.AuthorisationDTO;
import com.havryliv.auction.dto.UserDTO;
import com.havryliv.auction.entity.User;
import com.havryliv.auction.entity.UserRole;
import com.havryliv.auction.exception.BusinessException;
import com.havryliv.auction.exception.UserNotFoundException;
import com.havryliv.auction.repository.UserRepository;
import com.havryliv.auction.security.JwtTokenProvider;
import com.havryliv.auction.service.MailService;
import com.havryliv.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider jwtTokenProvider;

    private AuthenticationManager authenticationManager;

    private MailService mailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.mailService = mailService;
    }

    public AuthorisationDTO login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(UserNotFoundException::new);
            return  new AuthorisationDTO(jwtTokenProvider.createToken(username, user.getUserRoles()));
        } catch (AuthenticationException e) {
            throw new BusinessException(ExceptionMessages.USERNAME_OR_PASSWORD_INVALID, HttpStatus.UNAUTHORIZED);
        }
    }

    public AuthorisationDTO register(UserDTO userDTO) {
        User user = UserConverter.fromDTOtoEntity(userDTO);
        if (!userRepository.existsByUsername(user.getUsername()) && !userRepository.existsByEmail(user.getEmail())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUserRoles(Collections.singletonList(UserRole.ROLE_CLIENT));
            userRepository.save(user);
        } else {
            throw new BusinessException(ExceptionMessages.USERNAME_OR_EMAIL_ALREADY_IN_USE, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        mailService.sendMail("You are successfully registered", "Congratulations", user.getEmail());
        return new AuthorisationDTO (jwtTokenProvider.createToken(user.getUsername(), user.getUserRoles()));
    }

    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }

    public UserDTO search(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
      return UserConverter.fromEntityToDTO(user);

    }

    public UserDTO getCurrentUserByToken(HttpServletRequest req) {
        User user = userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)))
                .orElseThrow(UserNotFoundException::new);
       return UserConverter.fromEntityToDTO(user);
    }

    public String refresh(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        return jwtTokenProvider.createToken(username, user.getUserRoles());
    }

}
