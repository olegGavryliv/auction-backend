package com.havryliv.auction.service;

import com.havryliv.auction.dto.AuthorisationDTO;
import com.havryliv.auction.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService {

    AuthorisationDTO login(String username, String password);

    AuthorisationDTO register(UserDTO userDTO);

    void delete(String username);

    UserDTO search(String username);

    UserDTO getCurrentUserByToken(HttpServletRequest req);

    String refresh(String username);
}
