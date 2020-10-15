package controller;

import model.Customer;
import model.CustomerForm;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.CustomerService;
import service.ProvinceService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private Environment environment;

    @Autowired
    private CustomerService customerService;

    @Autowired
    ProvinceService provinceService;

    @ModelAttribute("provinces")
    public Iterable<Province> provinces(){
        return provinceService.findAll();
    }

    @GetMapping
    public ModelAndView listCustomers(@RequestParam("s")Optional<String>s,@PageableDefault(size = 3) Pageable pageable){
        Page<Customer> customers;
        ModelAndView modelAndView = new ModelAndView("/list");
        if (s.isPresent()){
            customers = customerService.findAllByFirstNameContaining(s.get(),pageable);
            modelAndView.addObject("s",s.get());
        }
        else {
            customers = customerService.findAll(pageable);
        }

        modelAndView.addObject("customers", customers);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("customer", new Customer());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveCustomer(@ModelAttribute("customer") CustomerForm customerForm){
        ModelAndView modelAndView = new ModelAndView("/create");
        Customer customer = new Customer(customerForm.getId(),customerForm.getFirstName(),customerForm.getLastName(),customerForm.getProvince());
        MultipartFile file = customerForm.getImage();
        String image = file.getOriginalFilename();
        customer.setImage(image);
        String fileUpload = environment.getProperty("file_upload").toString();
        try {
            FileCopyUtils.copy(file.getBytes(),new File(fileUpload+image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        customerService.save(customer);
        modelAndView.addObject("customer", new CustomerForm());
        modelAndView.addObject("message", "New customer created successfully");
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Customer customer = customerService.findById(id);
        if(customer != null) {
            ModelAndView modelAndView = new ModelAndView("/edit");
            modelAndView.addObject("customer", customer);
            return modelAndView;

        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/edit")
    public ModelAndView updateCustomer(@ModelAttribute("customer") CustomerForm customerForm){
        ModelAndView modelAndView = new ModelAndView("/edit");
        Customer updateCustomer = customerService.findById(customerForm.getId());
        updateCustomer.setId(customerForm.getId());
        updateCustomer.setFirstName(customerForm.getFirstName());
        updateCustomer.setLastName(customerForm.getLastName());
        updateCustomer.setProvince(customerForm.getProvince());

        MultipartFile file = customerForm.getImage();
        String image = file.getOriginalFilename();
        updateCustomer.setImage(image);
        String fileUpload = environment.getProperty("file_upload").toString();
        try {
            FileCopyUtils.copy(file.getBytes(),new File(fileUpload+image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        customerService.save(updateCustomer);
        modelAndView.addObject("message", "Customer updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Customer customer = customerService.findById(id);
        if(customer != null) {
            ModelAndView modelAndView = new ModelAndView("/delete");
            modelAndView.addObject("customer", customer);
            return modelAndView;

        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete")
    public String deleteCustomer(@ModelAttribute("customer") Customer customer){
        customerService.remove(customer.getId());
        return "redirect:customers";
    }
}
