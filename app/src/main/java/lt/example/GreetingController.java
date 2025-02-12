package lt.example;

import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("Greeting requested.");
        Greeting greeting = new Greeting(counter.incrementAndGet(), String.format(template, name));
        log.info("Greeted successfully with counter: " + counter);
        return greeting;
    }

    @GetMapping("/greeting/{name}")
    public Greeting greetingWithName(@PathVariable String name) {
        log.info("Greeting requested with name: " + name);
        Greeting greeting = new Greeting(counter.incrementAndGet(), String.format(template, name));
        log.info("Greeted successfully with counter: " + counter);
        return greeting;
    }

}