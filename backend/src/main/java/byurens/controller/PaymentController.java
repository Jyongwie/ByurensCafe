package byurens.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.dto.PaymentRequest;
import byurens.dto.PaymentResponse;
import byurens.dto.RefundRequest;
import byurens.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/payments")
@RequiredArgsConstructor 
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping 
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refund")
    public ResponseEntity<PaymentResponse> processRefund(@Valid @RequestBody RefundRequest request) {
        PaymentResponse response = paymentService.processRefund(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
