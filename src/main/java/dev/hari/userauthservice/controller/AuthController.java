package dev.hari.userauthservice.controller;

import dev.hari.userauthservice.dto.AuthRequest;
import dev.hari.userauthservice.dto.LoginResponse;
import dev.hari.userauthservice.dto.UserDto;
import dev.hari.userauthservice.model.Role;
import dev.hari.userauthservice.model.User;
import dev.hari.userauthservice.service.AuthService;
import dev.hari.userauthservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<UserDto> signUp(@RequestBody AuthRequest authRequest){

        User user = authService.signUp(authRequest.getEmail(), authRequest.getPassword());

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setId(user.getId());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest){
        String token = authService.login(authRequest.getEmail(), authRequest.getPassword());
        if(token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<UserDto> validateToken(@RequestHeader("Authorization") String header){
        if(!header.startsWith("Bearer ")) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        try{
            Claims claims = jwtService.parseToken(header.substring("Bearer ".length()));
            UserDto userDto = new UserDto();
            userDto.setEmail(claims.getSubject());
            userDto.setRole(Role.valueOf(claims.get("Role", String.class)));
            return ResponseEntity.ok(userDto);

        } catch (JwtException ex){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
