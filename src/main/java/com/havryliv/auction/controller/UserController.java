package com.havryliv.auction.controller;

import com.havryliv.auction.configuration.AppConfig;
import com.havryliv.auction.dto.AuthorisationDTO;
import com.havryliv.auction.dto.UserDTO;
import com.havryliv.auction.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {

    private UserService userService;

    private AppConfig appConfig;

    @Value("${app.environment}")
    private String environment;

    public UserController(UserService userService, AppConfig appConfig) {
        this.userService = userService;
        this.appConfig = appConfig;

    }

    @PostMapping("/login")
    @ApiOperation(value = "${UserController.login}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<AuthorisationDTO> login(@RequestBody @Valid UserDTO userDTO) {
        System.out.println(appConfig);
        System.out.println(environment);
        System.out.println(appConfig.getAuctionSettings());
        return ResponseEntity.ok()
                .body(userService.login(userDTO.getUsername(), userDTO.getPassword()));
    }

    @PostMapping("/register")
    @ApiOperation(value = "${UserController.register}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 422, message = "Username is already in use"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<AuthorisationDTO> register(@ApiParam("Signup User") @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(userDTO));
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${UserController.delete}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "The user doesn't exist"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity delete(@ApiParam("Username") @PathVariable String username) {
        userService.delete(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${UserController.search}", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "The user doesn't exist"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<UserDTO> search(@ApiParam("Username") @PathVariable String username) {
        return ResponseEntity.ok()
                .body(userService.search(username));
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${UserController.me}", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<UserDTO> currentUserByToken(HttpServletRequest req, Principal principal) {
        System.out.println(principal.getName());
        return ResponseEntity.ok()
                .body(userService.getCurrentUserByToken(req));
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> refresh(HttpServletRequest req) {
        return ResponseEntity.ok()
                .body(userService.refresh(req.getRemoteUser()));
    }

}
