package repository;

import model.Customer;
import model.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer,Long>{
    Iterable<Customer> findAllByProvince(Province province);

    Page<Customer>findAllByFirstNameContaining(String firstName, Pageable pageable);
}
