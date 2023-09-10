// package mini_project.server.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import mini_project.server.service.AuthService;
// import mini_project.server.service.SecurityTokenService;

// @RestController
// @RequestMapping("/api/auth")
// public class AuthController {
    
//     @Autowired
//     private AuthService authSvc;

//     @Autowired
//     private SecurityTokenService securityTokenService;

//     // @PostMapping("/register")
//     // public ResponseEntity<User> register(@RequestBody User user) {
//     //     // ...
//     // }

//     @PostMapping("/login")
//     public ResponseEntity<String> login(@ModelAttribute Authentication auth) {
//        String token = securityTokenService.generateToken(auth);

//        return ResponseEntity.ofNullable(token);
//     }

//     // @PostMapping("/logout")
//     // public ResponseEntity<String> logout() {
//     //     // ...
//     // }
// }
