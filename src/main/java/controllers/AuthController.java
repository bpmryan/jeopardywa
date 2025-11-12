package controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.UserInfo;
import repo.UserInfoRepo;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserInfoRepo userRepo;

    // Java function to generate a userId once they create an account 
    @PostMapping("/register")
    public UserInfo registerUser(@RequestBody UserInfo user) {
        user.setUserId("U" + String.format("%05d", (int)(Math.random() * 100000)));
        return userRepo.save(user);
    }

    // Checks whether or not the user inputed the correct credentials or not
    @PostMapping("/login")
    public String login(@RequestBody UserInfo user) {
        Optional<UserInfo> found = userRepo.findAll()
            .stream()
            .filter(u -> u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()))
            .findFirst();

        // If user is found in db, then print "Login successful", otherwise "Invalid Credentials"
        return found.isPresent() ? "Login Successful" : "Invalid Credentials";
    }
}
