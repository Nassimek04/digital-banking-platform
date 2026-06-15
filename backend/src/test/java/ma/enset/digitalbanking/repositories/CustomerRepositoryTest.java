package ma.enset.digitalbanking.repositories;

import ma.enset.digitalbanking.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldSaveAndSearchCustomer() {
        Customer c = new Customer();
        c.setName("TestUser");
        c.setEmail("test@bank.ma");
        customerRepository.save(c);

        List<Customer> found = customerRepository.searchCustomer("%Test%");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getName()).isEqualTo("TestUser");
    }

    @Test
    void shouldFindByNameContains() {
        Customer c = new Customer();
        c.setName("Mohamed");
        c.setEmail("mohamed@bank.ma");
        customerRepository.save(c);

        List<Customer> found = customerRepository.findByNameContainsIgnoreCase("moha");
        assertThat(found).hasSize(1);
    }
}
