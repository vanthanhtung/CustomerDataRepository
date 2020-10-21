package controller;

import javassist.NotFoundException;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ProvinceService;

@RestController
@RequestMapping("/api/provinces")
public class RestProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @GetMapping
    public ResponseEntity<Iterable<Province>> findAll(){
        return new ResponseEntity<>(provinceService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Province> findById(@PathVariable Long id){
        try {
            Province province = (Province) provinceService.findById(id);
            return new ResponseEntity<>(province,HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Province> createProvince(@RequestBody Province province){
        provinceService.save(province);
        return new ResponseEntity<>(province,HttpStatus.OK);
    }

    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<Province>editProvince(@PathVariable Long id,@RequestBody Province province){
        try{
            province.setId(id);
            provinceService.save(province);
            return new ResponseEntity<>(province,HttpStatus.OK);
        }
        catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Province>deleteProvince(@PathVariable Long id){
        try{
            provinceService.remove(id);
            return new ResponseEntity<>((Province) provinceService.findById(id),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
