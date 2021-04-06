package com.vti.company.repository;

import com.vti.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query("select entity FROM Company entity where entity.code =?1 ")
    List<Company> getByCode(String code);

    @Query("select count(entity.id) from Company entity where entity.code =?1 and (entity.id <> ?2 or ?2 is null) ")
    Long checkCode(String code, UUID id);
}
