package com.parthakadam.space.object_store.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parthakadam.space.object_store.configs.ObjectStoreConfig;
import com.parthakadam.space.object_store.models.Bucket;
import com.parthakadam.space.object_store.models.ObjectEntity;
import com.parthakadam.space.object_store.repository.ObjectRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@Service
public class ObjectServiceImp implements ObjectService {

    @Autowired
    BucketServiceImp bucketService;
    @Autowired
    ObjectStoreConfig objectStoreConfig;
    @Autowired
    ObjectRepository objectRepository;

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // /data/
    // ├── a3/
    // │ ├── a3f9c12e.bin
    // │ └── a3b8e21d.bin
    // ├── f9/
    // │ ├── f912ac33.bin

    public Path generateDataPath(String objectId) throws IOException {
        String shard = objectId.substring(0, 2);

        Path dir = Paths.get(objectStoreConfig.getUploadDir().toString(), shard);
        Files.createDirectories(dir);

        return dir.resolve(objectId + ".bin");
    }

    public ObjectEntity createObjectEntity(String bucketName, String objectKey) {
        Bucket bucket = bucketService.getBucketByName(bucketName);

        if (bucket == null) {
            throw new ValidationException("bucket does not exit");
        }
        if (objectRepository.existsByBucketIdAndObjectKey(bucket.getId(), objectKey)) {
            throw new ValidationException("duplicate key");

        }
        ;

        UUID objectIdUUID = UUID.randomUUID();
        String objectId = objectIdUUID.toString().replace("-", "");

        Path dataPath;
        try {
            dataPath = generateDataPath(objectId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate data path", e);
        }

        return ObjectEntity.builder()
                .id(objectIdUUID)
                .bucketId(bucket.getId())
                .objectKey(objectKey)
                .dataPath(dataPath.toString())
                .build();
    }

    @Transactional
    public ObjectEntity putObject(
            String bucketName,
            String objectKey,
            InputStream data,
            String contentType) {

        // 1. Create object metadata (no file yet)
        ObjectEntity object = createObjectEntity(bucketName, objectKey);
        Path path = Paths.get(object.getDataPath());

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }

        long size = 0;

        // 2. Write file to disk
        try (OutputStream out = Files.newOutputStream(
                path,
                StandardOpenOption.CREATE_NEW,
                StandardOpenOption.WRITE)) {

            byte[] buffer = new byte[8192];
            int read;

            while ((read = data.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                digest.update(buffer, 0, read);
                size += read;
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to write object data", e);
        }

        // 3. Finalize metadata
        object.setSizeBytes(size);
        object.setCreatedAt(OffsetDateTime.now());
        object.setContentType(contentType);
        object.setChecksumSha256(bytesToHex(digest.digest()));

        // 4. Save metadata in DB
        try {
            return objectRepository.save(object);
        } catch (Exception e) {
            // DB failed → rollback file
            try {
                Files.deleteIfExists(path);
            } catch (IOException ignored) {
            }
            throw e;
        }
    }

    @Transactional
    public ObjectEntity getObject(String bucketName, String objectKey) {
        ObjectEntity objectEntity = objectRepository.getObject(bucketName, objectKey);

        if (objectEntity == null) {
            throw new EntityExistsException("object not found");
        }

        try (InputStream is = Files.newInputStream(Paths.get(objectEntity.getDataPath()))) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            if (!objectEntity.getChecksumSha256().equals(bytesToHex(digest.digest()))) {
                throw new RuntimeException("data integrity check failed");
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("data integrity check failed", e);
        }

        return objectEntity;
    }

    @Transactional
    public void deleteObject(String bucketName, String objectKey) {
        ObjectEntity objectEntity = objectRepository.getObject(bucketName, objectKey);

        if (objectEntity == null) {
            throw new EntityExistsException("object not found");
        }

        Path path = Paths.get(objectEntity.getDataPath());

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("failed to delete object data", e);
        }

        objectRepository.delete(objectEntity);
    }

}
