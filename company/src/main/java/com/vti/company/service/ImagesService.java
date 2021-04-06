package com.vti.company.service;

import com.vti.company.domain.Images;
import com.vti.company.dto.ImagesDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;

public interface ImagesService extends IGenericService<ImagesDto, UUID> {

    ImagesDto store(MultipartFile file) throws IOException;

    ImagesDto getFile(UUID id);

    Stream<Images> getAllFiles();
}
