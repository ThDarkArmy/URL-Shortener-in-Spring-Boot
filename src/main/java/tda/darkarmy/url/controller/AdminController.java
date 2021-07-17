package tda.darkarmy.url.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.ResponseEntity.status;

@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/")
    public ResponseEntity<?> getAdmin(){
        return status(200).body("This is admin's area.");
    }
}
