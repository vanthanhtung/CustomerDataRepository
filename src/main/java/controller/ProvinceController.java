package controller;

import model.Customer;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import service.CustomerService;
import service.ProvinceService;

@Controller
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/provinces")
    public ModelAndView listProvinces(){
        Iterable<Province> listProvince = provinceService.findAll();
        ModelAndView modelAndView = new ModelAndView("/province/list");
        modelAndView.addObject("provinces",listProvince);
        return modelAndView;
    }

    @GetMapping("create-province")
    public ModelAndView showCreateForm(@ModelAttribute("province") Province province){
        ModelAndView modelAndView = new ModelAndView("/province/create");
        modelAndView.addObject("province",new Province());
        return modelAndView;
    }

    @PostMapping("create-province")
    public ModelAndView saveProvince(@ModelAttribute("province") Province province){
        ModelAndView modelAndView = new ModelAndView("/province/create");
        provinceService.save(province);
        modelAndView.addObject("province",new Province());
        modelAndView.addObject("message","Success!!!");
        return modelAndView;
    }

    @GetMapping("edit-province/{id}")
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

    @PostMapping("edit-province")
    public ModelAndView updateProvince(@ModelAttribute("province") Province province){
        provinceService.save(province);
        ModelAndView modelAndView = new ModelAndView("/province/edit");
        modelAndView.addObject("message","Edit success!!!");
        return modelAndView;
    }

    @GetMapping("delete-province/{id}")
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

    @PostMapping("delete-province")
    public ModelAndView deleteProvince(@ModelAttribute("province") Province province){
        provinceService.remove(province.getId());
        ModelAndView modelAndView = new ModelAndView("redirect:/provinces");
        return modelAndView;
    }

    @GetMapping("/view-province/{id}")
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
}
