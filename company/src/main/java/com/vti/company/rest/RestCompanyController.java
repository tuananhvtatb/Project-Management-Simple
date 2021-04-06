package com.vti.company.rest;

import com.vti.company.dto.CompanyDto;
import com.vti.company.exception.IdenticalCodeException;
import com.vti.company.exception.NotFoundException;
import com.vti.company.funtiondto.CompanySearchDto;
import com.vti.company.service.CompanySerVice;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping("/api/company")
public class RestCompanyController {

    private final CompanySerVice companySerVice;

    public RestCompanyController(CompanySerVice companySerVice) {
        this.companySerVice = companySerVice;
    }

    @PostMapping(value = "/searchByDto")
    public ResponseEntity<Page<CompanyDto>> searchByDto(@RequestBody CompanySearchDto dto) {
        Page<CompanyDto> result = companySerVice.searchByDto(dto);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CompanyDto> getById(@PathVariable UUID id) {
        CompanyDto result = companySerVice.getById(id);
        if(result == null){
            throw new NotFoundException("Invalid company id:"+ id);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/getByCode/{code}")
    public ResponseEntity<CompanyDto> getByCode(@PathVariable String code) {
        CompanyDto result = companySerVice.getByCode(code);
        if(result == null){
            throw new NotFoundException("Invalid company code:"+ code);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CompanyDto> save(@Valid @RequestBody CompanyDto dto){
        boolean x = companySerVice.checkCode(dto.getId(), dto.getCode());
        if(x){
            throw new IdenticalCodeException("Code "+dto.getCode()+" is used!");
        }
        CompanyDto result = companySerVice.saveOrUpdate(dto, dto.getId());
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CompanyDto> update(@Valid @RequestBody CompanyDto dto, @PathVariable("id") UUID id) {
        boolean x = companySerVice.checkCode(id, dto.getCode());
        if(x){
            throw new IdenticalCodeException("Code "+dto.getCode()+" is used!");
        }
        CompanyDto result = companySerVice.saveOrUpdate(dto, id);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") UUID id) {
        Boolean result = companySerVice.deleteById(id);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/checkCode")
    public ResponseEntity<Boolean> checkCode(@RequestParam(value = "id", required=false) UUID id, @RequestParam("code") String code) {
        Boolean result = companySerVice.checkCode(id, code);
        return new ResponseEntity<>(result, (result != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
