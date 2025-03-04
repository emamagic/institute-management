package com.emamagic.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
@RequiredArgsConstructor
public class FallbackController {

    private final MessageSourceAccessor messageSourceAccessor;

    @GetMapping("/service-unavailable")
    public ResponseEntity<String> serviceUnavailable() {
        return ResponseEntity.internalServerError().body(messageSourceAccessor.getMessage("CIRCUIT_FALLBACK_ERROR"));
    }

}
