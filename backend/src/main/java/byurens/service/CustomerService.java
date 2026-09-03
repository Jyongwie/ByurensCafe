package byurens.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.CustomerResponse;
import byurens.dto.NewCustomerRequest;
import byurens.entities.Customer;
import byurens.entities.User;
import byurens.exception.ByurensCafeException;
import byurens.repository.CustomerRepository;
import byurens.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CustomerResponse newCustomer(NewCustomerRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ByurensCafeException("Email already been registered");
        }

        User user = User.builder()
            .email(request.email())
            .passwordHash(passwordEncoder.encode(request.rawPassword()))
            .phoneNumber(request.phoneNumber())
            .build();

        User savedUser = userRepository.save(user);

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
