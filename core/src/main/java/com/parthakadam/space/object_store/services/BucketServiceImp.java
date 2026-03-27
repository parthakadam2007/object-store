package com.parthakadam.space.object_store.services;

import com.parthakadam.space.object_store.configs.WebClientAuth;
import com.parthakadam.space.object_store.dto.AccessTokenResponceDTO;
import com.parthakadam.space.object_store.dto.BucketResponseAccessTokenDTO;
import com.parthakadam.space.object_store.models.BodyAccessCreateAuth;
import com.parthakadam.space.object_store.models.Bucket;
import com.parthakadam.space.object_store.repository.BucketRepository;

import jakarta.validation.ValidationException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;


@Service
@Transactional
public class BucketServiceImp implements BucketService {

    private final BucketRepository bucketRepository;
    private final RegionService regionService;
    private final WebClientAuth webClientAuth;
    private final ExecutorService executorService;
    private final ObjectMapper objectMapper;

    BucketServiceImp(BucketRepository bucketRepository, RegionService regionService, WebClientAuth webClientAuth, ExecutorService executorService, ObjectMapper objectMapper) {
        this.bucketRepository = bucketRepository;
        this.regionService = regionService;
        this.webClientAuth = webClientAuth;
        this.executorService = executorService;
        this.objectMapper = objectMapper;
    }


    @Override
    public BucketResponseAccessTokenDTO createBucket(String name, String region_input) {
        Bucket bucket = bucketRepository.findByName(name);
        System.out.println("region_input");
        System.out.println(region_input);
        if (bucket != null) {
            throw new ValidationException("duplicate bucket name");
        }

        if (!regionService.isValidRegion(region_input)) {
            throw new ValidationException("region not defined");
        }

        UUID bucketId = UUID.randomUUID();
        BodyAccessCreateAuth bodyAccessCreateAuth = new BodyAccessCreateAuth(bucketId);

        Bucket newBucket = Bucket.builder()
                .name(name)
                .region(region_input)
                .createdAt(OffsetDateTime.now())
                .id(bucketId)

                .build();
//        Bucket result = bucketRepository.save(newBucket);

        CompletableFuture<Bucket> resultBucket = CompletableFuture.supplyAsync(() -> bucketRepository.save(newBucket), executorService).exceptionally(ex ->{
//                    return "failed to save bucket";
                throw  new RuntimeException(ex);
                });    //create token
        CompletableFuture<String> resultAuth = CompletableFuture.supplyAsync(
                () -> webClientAuth.client.post()
                        .uri("/createAccessSecret")
                        .header("Content-Type", "application/json")
                        .bodyValue(bodyAccessCreateAuth)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block(), executorService
        ).exceptionally(ex ->{
            return "failed to create  auth token";
        });

        CompletableFuture<Void> all = CompletableFuture.allOf(resultBucket, resultAuth);
        all.join();

        AccessTokenResponceDTO accessTokenResponceDTO = objectMapper.readValue(resultAuth.join(), AccessTokenResponceDTO.class);
        Bucket bucketResponse = resultBucket.join();

        BucketResponseAccessTokenDTO bucketResponseAccessTokenDTO = BucketResponseAccessTokenDTO.builder().bucket(bucketResponse).accessTokenResponceDTO(accessTokenResponceDTO).build();
        System.out.println("resultAuth - " + resultAuth.join());

        return bucketResponseAccessTokenDTO;
    }

    @Override
    public List<Bucket> getBuckets() {
        return bucketRepository.findAll();
    }

    @Override
    public Bucket getBucketByName(String name) {
        return bucketRepository.findByName(name);
    }
}
