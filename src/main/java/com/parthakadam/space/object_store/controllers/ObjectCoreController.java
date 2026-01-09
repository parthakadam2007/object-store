package com.parthakadam.space.object_store.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.parthakadam.space.object_store.dto.BucketCreateRequestDTO;
import com.parthakadam.space.object_store.dto.BucketResponseDTO;
import com.parthakadam.space.object_store.mapper.BucketMapper;
import com.parthakadam.space.object_store.models.Bucket;
import com.parthakadam.space.object_store.services.BucketServiceImp;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class ObjectCoreController {


    @Autowired 
    private BucketServiceImp bucketService;

    //////////////Bucket 
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
    ////////////////put/////
    @PostMapping("/buckets")
    public ResponseEntity<BucketResponseDTO> createBucket(
            @Valid @RequestBody BucketCreateRequestDTO dto) {

        Bucket bucket = bucketService.createBucket(dto.getName(), dto.getRegion());
        BucketResponseDTO response = BucketMapper.toDTO(bucket);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

 
    
    //TODO Add db for metadata
//    @PostMapping("/{bucket}/{key}")
//    public StoredObject upload(
//             @PathVariable String bucket,
//             @PathVariable String key,
//             @RequestParam MultipartFile file,
//             @RequestParam(required = false) Map<String, String> metadata
//     ) throws Exception {

//         return storage.putObject(
//                 bucket,
//                 key,
//                 file.getInputStream(),
//                 file.getContentType(),
//                 metadata
//         );
//     }

//     @GetMapping("/{bucket}/{key}")
//     public void download(
//             @PathVariable String bucket,
//             @PathVariable String key,
//             HttpServletResponse response
//     ) throws Exception {

//         var input = storage.getObject(bucket, key);
//         input.transferTo(response.getOutputStream());
//     }

//     @DeleteMapping("/{bucket}/{key}")
//     public void delete(
//             @PathVariable String bucket,
//             @PathVariable String key
//     ) throws Exception {
//         storage.deleteObject(bucket, key);
//     }
    

    
}