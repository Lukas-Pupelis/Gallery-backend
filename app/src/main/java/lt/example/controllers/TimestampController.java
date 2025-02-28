package lt.example.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class TimestampController {

    @GetMapping("/api/time")
    public ResponseEntity<String> getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        return ResponseEntity.ok(now.toString());
    }

}