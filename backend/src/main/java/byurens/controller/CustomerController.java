package byurens.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.dto.CustomerResponse;
import byurens.dto.NewCustomerRequest;
import byurens.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/customers")
@RequiredArgsConstructor 
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping 
    public ResponseEntity<CustomerResponse> newCustomer(@Valid @RequestBody NewCustomerRequest request) {
        CustomerResponse response = customerService.newCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping 
    public ResponseEntity<List<CustomerResponse>> getCustomers() {
        List<CustomerResponse> responses = customerService.getCustomers();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }
}
