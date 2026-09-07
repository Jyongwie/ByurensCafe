package byurens.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
