package lt.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsertController {

    @GetMapping("/insert")
    public ResponseEntity<String> insertImage() {
        return ResponseEntity.ok(toString());
    }

}
