package com.vti.company.service;

import com.vti.company.domain.Company;
import com.vti.company.dto.CompanyDto;
import com.vti.company.funtiondto.CompanySearchDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CompanySerVice extends IGenericService<Company, UUID>{
    Page<CompanyDto> searchByDto(CompanySearchDto dto);

    CompanyDto saveOrUpdate(CompanyDto dto, UUID id);

    CompanyDto getById(UUID id);

    Boolean deleteById(UUID id);

    Boolean checkCode(UUID id,String code);

    CompanyDto getByCode(String code);
}
