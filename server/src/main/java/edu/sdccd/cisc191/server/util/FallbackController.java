package edu.sdccd.cisc191.server.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/**")
    public ResponseEntity<String> handleFallback() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=UTF-8");

        return ResponseEntity.ok()
                .headers(headers)
                .body("<html><head></head><body></body></html>");
    }
}
