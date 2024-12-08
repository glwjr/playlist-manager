package edu.sdccd.cisc191.server.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @RequestMapping("/**")
    public ResponseEntity<String> handleFallback() {
        String blankPage = "<html><head></head><body></body></html>";
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(blankPage);
    }
}
