package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.example.vo.ReqBody;
import org.example.vo.RespData;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Api(value = "API測試", tags = "API測試")
public class SwaggerController {
    ObjectMapper mapper = new ObjectMapper();
    static Map<String, List<String>> cars = new HashMap<>();

    static {
        //Arrays.asList returns a non-resizable List
        cars.put("Toyota",new ArrayList<String>(Arrays.asList("Yaris","Altis","Vios")));
        cars.put("Honda",new ArrayList<String>(Arrays.asList("Fit","CRV","Civic")));
        cars.put("Ford",new ArrayList<String>(Arrays.asList("Focus","Kuga","Wagon")));
    }

    @ApiOperation(value = "取得車子型號", notes = "根據品牌取得車子型號")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "brand", value = "車子品牌", required = true, dataType = "String")
    })
    @GetMapping("/getCars/{brand}")
    public ResponseEntity<Object> getCarByBrand(@PathVariable("brand") String brand) throws JsonProcessingException {

        return new ResponseEntity<>(cars.get(brand), HttpStatus.OK);
    }

    @ApiOperation(value = "新增車子資訊", notes = "新增品牌及車子型號")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "新增車子資訊", required = true, dataType = "ReqBody")
    })
    @PostMapping("/addCar")
    public ResponseEntity<Object> addCarBrand(@RequestBody ReqBody body) throws JsonProcessingException {

        if(cars.containsKey(body.getBrand())){
            cars.get(body.getBrand()).add(body.getModel());
            List<String> models = cars.get(body.getBrand()).stream().distinct().collect(Collectors.toList());
            cars.put(body.getBrand(), models);
        }else{
            cars.put(body.getBrand(), Arrays.asList(body.getModel()));
        }
        return new ResponseEntity<>(cars, HttpStatus.CREATED);
    }

    //@ApiIgnore
    @GetMapping("/error")
    public void empty(){
        throw new RuntimeException("異常");
    }
}
