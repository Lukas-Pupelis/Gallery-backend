package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
public class TimestampController {

    @GetMapping("/time")
    public ResponseEntity<String> getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        return ResponseEntity.ok(now.toString());
    }

}
