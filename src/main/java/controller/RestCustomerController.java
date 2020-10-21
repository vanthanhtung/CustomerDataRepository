package controller;

import model.Customer;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CustomerService;
import service.ProvinceService;

@RestController
@RequestMapping("/api/customers")
public class RestCustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    ProvinceService provinceService;

    @ModelAttribute("customers")
    public Iterable<Customer> customers(){
        return customerService.findAll();
    }

    @GetMapping
    public ResponseEntity<Iterable<Customer>> findAll(){
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id){
        try {
            Customer customer = customerService.findById(id);
            return new ResponseEntity<>(customer,HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        customerService.save(customer);
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }

    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<Customer>editCustomer(@PathVariable Long id,@RequestBody Customer customer){
        try{
            customer.setId(id);
            customerService.save(customer);
            return new ResponseEntity<>(customer,HttpStatus.OK);
        }
        catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE  )
    public ResponseEntity<Customer>deleteCustomer(@PathVariable Long id){
        try{
            customerService.remove(id);
            return new ResponseEntity<>(customerService.findById(id),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
