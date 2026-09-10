package byurens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController 
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor 
public class WebhookController {
    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<Void> handlePaymentWebhook(
        @RequestHeader("Signature-Header") String signature,
        @RequestBody String payload
    ) {
        log.info("Received payment webhook notification");
        paymentService.processPaymentWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }
}