package com.vti.company.service.impl;

import com.vti.company.domain.Images;
import com.vti.company.dto.ImagesDto;
import com.vti.company.repository.ImageRepository;
import com.vti.company.service.ImagesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Transactional
@Service
public class ImageService implements ImagesService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public ImagesDto store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Images images = new Images(fileName, file.getContentType(), file.getBytes());
        images.setCreatedDate(LocalDateTime.now());
        imageRepository.save(images);
        return new ImagesDto(images);
    }

    @Override
    public ImagesDto getFile(UUID id) {
        Images image = imageRepository.findById(id).get();
        return new ImagesDto(image);
    }

    @Override
    public Stream<Images> getAllFiles() {
        return imageRepository.findAll().stream();
    }
}
