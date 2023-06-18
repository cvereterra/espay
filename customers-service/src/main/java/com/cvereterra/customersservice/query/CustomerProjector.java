package com.cvereterra.customersservice.query;

import com.cvereterra.espaycore.events.customer.CustomerCreatedEvent;
import com.cvereterra.espaycore.queries.customer.FindCustomerByIdQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerProjector {
    private final CustomerRepository customerRepository;

    public CustomerProjector(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @EventHandler
    public void on(CustomerCreatedEvent event) {
        Optional<CustomerView> customer = customerRepository.findById(event.getCustomerId());

        if (customer.isPresent()) {
            throw new IllegalStateException("Customer already exists");
        }

        CustomerView customerView = new CustomerView(
                event.getCustomerId(),
                event.getName(),
                event.getEmail(),
                event.getAddress()
        );
        customerRepository.save(customerView);
    }

    @QueryHandler
    public CustomerView handle(FindCustomerByIdQuery query) {
        return customerRepository.findById(query.getCustomerId()).orElse(null);
    }
}
