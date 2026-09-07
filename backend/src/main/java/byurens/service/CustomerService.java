package byurens.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.CustomerResponse;
import byurens.dto.NewCustomerRequest;
import byurens.entities.Customer;
import byurens.entities.User;
import byurens.exception.ByurensCafeException;
import byurens.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    @Transactional
    public CustomerResponse newCustomer(NewCustomerRequest request) {
        User savedUser = userService.createUserAccount(
            request.email(),
            request.phoneNumber(),
            request.rawPassword()
        );

        Customer customer = Customer.builder()
            .user(savedUser)
            .name(request.name())
            .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    public List<CustomerResponse> getCustomers() {
        return customerRepository.findAll().stream()
            .map(this::mapToResponse).toList();
    }

    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Customer not found"));
        return mapToResponse(customer);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return new CustomerResponse(
            customer.getId(),
            customer.getName(),
            customer.getUser().getEmail(),
            customer.getUser().getPhoneNumber(),
            customer.getLoyaltyPoint(),
            customer.getWalletBalance()
        );
    }
}
