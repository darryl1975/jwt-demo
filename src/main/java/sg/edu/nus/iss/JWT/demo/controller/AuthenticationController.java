package sg.edu.nus.iss.JWT.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sg.edu.nus.iss.JWT.demo.payload.AuthenticationRequest;
import sg.edu.nus.iss.JWT.demo.payload.AuthenticationResponse;
import sg.edu.nus.iss.JWT.demo.payload.RegisterRequest;
import sg.edu.nus.iss.JWT.demo.service.AuthenticationService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        System.out.println("AuthenticationController (register request): " + request);
        AuthenticationResponse ar = service.register(request);

        return ResponseEntity.ok(ar);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        System.out.println("AuthenticationController (authenticate request): " + request);
        AuthenticationResponse ar = service.authenticate(request);

        String jwt = ar.getToken();
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        // cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // Global
        response.addCookie(cookie);
        return ResponseEntity.ok(ar);
    }
}
