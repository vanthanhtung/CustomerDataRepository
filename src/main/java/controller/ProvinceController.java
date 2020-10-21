package controller;

import model.Customer;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import service.CustomerService;
import service.ProvinceService;

@Controller
@RequestMapping("/provinces")
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ModelAndView listProvinces(){
        Iterable<Province> listProvince = provinceService.findAll();
        ModelAndView modelAndView = new ModelAndView("/province/list");
        modelAndView.addObject("provinces",listProvince);
        return modelAndView;
    }

    @GetMapping("create")
    public ModelAndView showCreateForm(@ModelAttribute("province") Province province){
        ModelAndView modelAndView = new ModelAndView("/province/create");
        modelAndView.addObject("province",new Province());
        return modelAndView;
    }

    @PostMapping("create")
    public ModelAndView saveProvince(@ModelAttribute("province") Province province){
        ModelAndView modelAndView = new ModelAndView("/province/create");
        provinceService.save(province);
        modelAndView.addObject("province",new Province());
        modelAndView.addObject("message","Success!!!");
        return modelAndView;
    }

    @GetMapping("edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Province province = (Province) provinceService.findById(id);
        if (province!=null){
            ModelAndView modelAndView = new ModelAndView("/province/edit");
            modelAndView.addObject("province",province);
            return modelAndView;
        }
        else {
            ModelAndView modelAndView = new ModelAndView("/province/error.404");
            return modelAndView;
        }
    }

    @PostMapping("edit")
    public ModelAndView updateProvince(@ModelAttribute("province") Province province){
        provinceService.save(province);
        ModelAndView modelAndView = new ModelAndView("/province/edit");
        modelAndView.addObject("message","Edit success!!!");
        return modelAndView;
    }

    @GetMapping("delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Province province = (Province) provinceService.findById(id);
        if (province!=null){
            ModelAndView modelAndView = new ModelAndView("/province/delete");
            modelAndView.addObject("province",province);
            return modelAndView;
        }
        else {
            ModelAndView modelAndView = new ModelAndView("/province/error.404");
            return modelAndView;
        }
    }

    @PostMapping("delete")
    public ModelAndView deleteProvince(@ModelAttribute("province") Province province){
        provinceService.remove(province.getId());
        ModelAndView modelAndView = new ModelAndView("redirect:/provinces");
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewProvince(@PathVariable Long id){
        Province province = (Province) provinceService.findById(id);
        if (province==null){
            ModelAndView modelAndView = new ModelAndView("/province/error.404");
            return modelAndView;
        }

        Iterable<Customer> customers = customerService.findAllByProvince(province);
        ModelAndView modelAndView = new ModelAndView("/province/view");
        modelAndView.addObject("province",province);
        modelAndView.addObject("customers",customers);
        return modelAndView;
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Province findById(@PathVariable Long id){
        return (Province) provinceService.findById(id);
    }
}
