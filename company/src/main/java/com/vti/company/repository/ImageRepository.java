package com.vti.company.repository;

import com.vti.company.domain.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Images, UUID> {
}
