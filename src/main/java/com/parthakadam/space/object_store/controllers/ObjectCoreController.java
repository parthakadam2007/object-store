package com.parthakadam.space.object_store.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.parthakadam.space.object_store.dto.BucketCreateRequestDTO;
import com.parthakadam.space.object_store.dto.BucketResponseDTO;
import com.parthakadam.space.object_store.mapper.BucketMapper;
import com.parthakadam.space.object_store.models.Bucket;
import com.parthakadam.space.object_store.models.ObjectEntity;
import com.parthakadam.space.object_store.services.BucketServiceImp;
import com.parthakadam.space.object_store.services.ObjectService;
import com.parthakadam.space.object_store.services.ObjectServiceImp;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import jakarta.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class ObjectCoreController {

    @Autowired
    private BucketServiceImp bucketService;
    @Autowired
    private ObjectServiceImp objectService;

    ////////////// Bucket
    // //////////GET

    /**
     * @return name of bucket
     */
    @GetMapping("/buckets")
    public ResponseEntity<List<BucketResponseDTO>> getBuckets() {

        List<BucketResponseDTO> result = bucketService.getBuckets()
                .stream()
                .map(BucketMapper::toDTO)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/keys")
    public String getKeys() {
        return new String();
    }

    //////////////// post                                   /////
    @PostMapping("/buckets")
    public ResponseEntity<BucketResponseDTO> createBucket(
            @Valid @RequestBody BucketCreateRequestDTO dto) {

        Bucket bucket = bucketService.createBucket(dto.getName(), dto.getRegion());
        BucketResponseDTO response = BucketMapper.toDTO(bucket);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

     @PostMapping("/{bucket}/{key}")
        public ResponseEntity<ObjectEntity> upload(
                @PathVariable String bucket,
                @PathVariable String key,
                @RequestParam("file") MultipartFile file) {

            if (file.isEmpty()) {
                throw new ValidationException("File is empty");
            }

            ObjectEntity object;
            try {
                object = objectService.putObject(
                        bucket,
                        key,
                        file.getInputStream(),
                        file.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read uploaded file", e);
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(object);
        }
        
    @GetMapping("/{bucket}/{key}")
    public ResponseEntity<Resource> getObject(
            @PathVariable String bucket,
            @PathVariable String key
    ) {

        ObjectEntity object = objectService.getObject(bucket, key);

        Path path = Paths.get(object.getDataPath());
        if (!Files.exists(path)) {
            throw new RuntimeException("Object data missing on disk");
        }

        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid object path", e);
        }

        return ResponseEntity.ok()
                .contentType(
                        object.getContentType() != null
                                ? MediaType.parseMediaType(object.getContentType())
                                : MediaType.APPLICATION_OCTET_STREAM
                )
                .contentLength(object.getSizeBytes())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + object.getObjectKey() + "\""
                )
                .header("X-Checksum-SHA256", object.getChecksumSha256())
                .body(resource);
    }

    @DeleteMapping("/{bucket}/{key}")
    public ResponseEntity<String> deleteObject(
            @PathVariable String bucket,
            @PathVariable String key
    ) {

        objectService.deleteObject(bucket, key);
        return ResponseEntity.ok("Object deleted successfully");
    }

    @PutMapping("/{bucket}/{key}")
    public ResponseEntity<ObjectEntity> putMethodName(
        @PathVariable String bucket,
         @PathVariable String key,
         @RequestParam("file") MultipartFile file
        ) {

            if (file.isEmpty()) {
                throw new ValidationException("File is empty");
            }
            objectService.deleteObject(bucket, key);
                        ObjectEntity object;
            try {
                object = objectService.putObject(
                        bucket,
                        key,
                        file.getInputStream(),
                        file.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read uploaded file", e);
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(object);
    }

}