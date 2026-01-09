package com.parthakadam.space.object_store.services;

import com.parthakadam.space.object_store.models.Bucket;
import com.parthakadam.space.object_store.repository.BucketRepository;

import jakarta.validation.ValidationException;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class BucketServiceImp implements BucketService {

    @Autowired private  BucketRepository bucketRepository;
    @Autowired private  RegionService regionService;
    

    @Override
    public Bucket createBucket(String name, String  region_input) {
        Bucket bucket  = bucketRepository.findByName(name);
        System.out.println("region_input");
        System.out.println(region_input);
        if (  bucket !=null){
            throw new ValidationException("duplicate bucket name");
        };       
        
        // TODO fix this 
        if (regionService.isValidRegion(region_input)){
            throw new ValidationException("region not defined");
        }

        Bucket newBucket = Bucket.builder()
                            .name(name)
                            .region(region_input)
                            .createdAt(OffsetDateTime.now())
                            .build();
        Bucket result = bucketRepository.save(newBucket);
        return result;
    }

    @Override
    public List<Bucket> getBuckets(){
        return bucketRepository.findAll();
    }

    
}
