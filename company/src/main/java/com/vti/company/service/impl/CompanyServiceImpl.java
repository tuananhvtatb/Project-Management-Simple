package com.vti.company.service.impl;

import com.vti.company.domain.Company;
import com.vti.company.domain.Images;
import com.vti.company.dto.CompanyDto;
import com.vti.company.funtiondto.CompanySearchDto;
import com.vti.company.repository.CompanyRepository;
import com.vti.company.service.CompanySerVice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class CompanyServiceImpl extends GenericServiceImpl<Company, UUID> implements CompanySerVice {

    private final CompanyRepository companyRepository;

    private final EntityManager manager;

    public CompanyServiceImpl(CompanyRepository companyRepository, EntityManager manager) {
        this.companyRepository = companyRepository;
        this.manager = manager;
    }

    @Override
    public Page<CompanyDto> searchByDto(CompanySearchDto dto) {
        if (dto == null) {
            return null;
        }
        int pageIndex = dto.getPageIndex();
        int pageSize = dto.getPageSize();

        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }

        String whereClause = " AND flag_delete = null";
        String sqlCount = "select count(comp.id) from Company as comp where (1=1) ";
        String sql = "select new com.vti.company.dto.CompanyDto(comp) from Company as comp where (1=1) ";

        if (dto.getTextName() != null && StringUtils.hasText(dto.getTextName())) {
            whereClause += " AND (comp.name LIKE :textName ) ";
        }

        if (dto.getTextCode() != null && StringUtils.hasText(dto.getTextCode())) {
            whereClause += " AND (comp.code LIKE :textCode ) ";
        }

        if (dto.getTextEmail() != null && StringUtils.hasText(dto.getTextEmail())) {
            whereClause += " AND (comp.email LIKE :textEmail ) ";
        }

        if (dto.getTextPhone() != null && StringUtils.hasText(dto.getTextPhone())) {
            whereClause += " AND (comp.phoneNumber LIKE :textPhone ) ";
        }

        if (dto.getTextStatus() != null && StringUtils.hasText(dto.getTextStatus())) {
            whereClause += " AND (comp.status LIKE :textStatus ) ";
        }

        sql += whereClause ;
        sqlCount += whereClause;
        Query q = manager.createQuery(sql, CompanyDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getTextName() != null && StringUtils.hasText(dto.getTextName())) {
            q.setParameter("textName", '%' + dto.getTextName().trim() + '%');
            qCount.setParameter("textName", '%' + dto.getTextName().trim() + '%');
        }

        if (dto.getTextCode() != null && StringUtils.hasText(dto.getTextCode())) {
            q.setParameter("textCode", '%' + dto.getTextCode().trim() + '%');
            qCount.setParameter("textCode", '%' + dto.getTextCode().trim() + '%');
        }

        if (dto.getTextEmail() != null && StringUtils.hasText(dto.getTextEmail())) {
            q.setParameter("textEmail", '%' + dto.getTextEmail().trim() + '%');
            qCount.setParameter("textEmail", '%' + dto.getTextEmail().trim() + '%');
        }

        if (dto.getTextPhone() != null && StringUtils.hasText(dto.getTextPhone())) {
            q.setParameter("textPhone", '%' + dto.getTextPhone().trim() + '%');
            qCount.setParameter("textPhone", '%' + dto.getTextPhone().trim() + '%');
        }

        if (dto.getTextStatus() != null && StringUtils.hasText(dto.getTextStatus())) {
            q.setParameter("textStatus", '%' + dto.getTextStatus().trim() + '%');
            qCount.setParameter("textStatus", '%' + dto.getTextStatus().trim() + '%');
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<CompanyDto> dtos = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<CompanyDto> result;
        result = new PageImpl<>(dtos, pageable, count);

        return result;
    }

    @Override
    public CompanyDto saveOrUpdate(CompanyDto dto, UUID id) {
        if (dto != null ) {
            Company entity = null;
            if (id != null) {
                entity = companyRepository.getOne(id);
                entity.setUpdatedDate(LocalDateTime.now());
            }
            if (entity == null && !checkCode(id, dto.getCode())) {
                entity = new Company();
            }
//            }else {
//                return null;
//            }

            entity.setName(dto.getName());
            entity.setCode(dto.getCode());
            entity.setEmail(dto.getEmail());
            entity.setAddress(dto.getAddress());
            entity.setPhoneNumber(dto.getPhoneNumber());
            entity.setStatus(dto.getStatus());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setFlagDelete(0);
            entity = companyRepository.save(entity);
            if (entity != null) {
                return new CompanyDto(entity);
            }
        }
        return null;
    }

    @Override
    public CompanyDto getById(UUID id) {
        if (id != null) {
            Company entity = companyRepository.getOne(id);
            if (entity != null) {
                return new CompanyDto(entity);
            }
        }
        return null;
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (id != null) {
            Company entity = companyRepository.getOne(id);
            if (entity != null) {
                entity.setFlagDelete(1);
                companyRepository.save(entity);
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean checkCode(UUID id, String code) {
        if (code != null && StringUtils.hasText(code)) {
            List<Company> entities = null;

            entities = companyRepository.getByCode(code);
            if (entities != null && entities.size()>0) {
                if (id != null && entities.get(0).getId().equals(id)) {
                    return false;
                }
                return true;
            }
            return false;
        }
        return null;
    }

    @Override
    public CompanyDto getByCode(String code) {
        if (StringUtils.hasText(code)) {
            List<Company> entities = companyRepository.getByCode(code);
            if (entities != null && !entities.isEmpty()) {
                return new CompanyDto(entities.get(0));
            }
        }
        return null;
    }
}
