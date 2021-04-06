package com.vti.company.rest;

import com.vti.company.dto.ImagesDto;
import com.vti.company.message.ResponeFile;
import com.vti.company.message.ResponeMessage;
import com.vti.company.service.ImagesService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/files")
public class RestImagesController {

    private final ImagesService imagesService;

    public RestImagesController(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @PostMapping()
    public ResponseEntity<ResponeMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            imagesService.store(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponeMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponeMessage(message));
        }
    }

    @GetMapping()
    public ResponseEntity<List<ResponeFile>> getListFiles() {
        List<ResponeFile> files = imagesService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId().toString())
                    .toUriString();

            return new ResponeFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID id) {
        ImagesDto fileDB = imagesService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getTitle() + "\"")
                .body(fileDB.getData());
    }
}
