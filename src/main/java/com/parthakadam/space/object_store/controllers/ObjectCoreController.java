package com.parthakadam.space.object_store.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class ObjectCoreController {
    // TODO
    /**
     * @return name of bucket
     */
    @GetMapping("/bucket")
    public ResponseEntity<String> getBucketName() {
        return ResponseEntity.ok("TODO");
    }

    @GetMapping("/keys")
    public String getKeys() {
        return new String();
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