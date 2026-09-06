package byurens.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.CustomerResponse;
import byurens.dto.NewCustomerRequest;
import byurens.entities.Customer;
import byurens.entities.User;
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
