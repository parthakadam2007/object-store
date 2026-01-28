# cloud native object store 

**Simple object storage service (Spring Boot)**
This is very immature way of implementing an claude native object store I have skiped gateway layer(auth),no replication,and currently every thing is running on single host and on single thread and on single storage device :). But it can give you a simple understanding of object-store working

A small Spring Boot service for storing objects (files) on disk and recording metadata in PostgreSQL. This repo is intended as a demo / starting point for a self-hosted object store and includes basic endpoints for creating buckets, uploading files, and downloading objects.

---

## Features ✅

- Create and list buckets
- Upload objects (multipart file upload)
- Download objects (streamed resource) with checksum header

---
## Future feature:
This features are very keen to make it scalabe 
1. Implementing authentication
  - Rate limiting (per access key / IP)
  - Request signing (AWS S3 style HMAC)
  - Request signing (AWS S3 style HMAC)

2. Metadata Service
Now object store is directly writing to DB (which is wrong for multiple resone)

3. failure handling

4. Split Object Core into Sub-Managers

  Object Core Manager
  ├── Placement Engine
  ├── Replication Manager
  ├── Health Monitor
  ├── Rebalancer
  └── Garbage Collector



## Quickstart ⚡

Prerequisites

- Java 21
- Maven 3.8+
- PostgreSQL

1. Configure the DB in `src/main/resources/application.properties` (default values used in repo):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/objectStore
spring.datasource.username=postgres
spring.datasource.password=1234
spring.datasource.driver-class-name=org.postgresql.Driver
```

2. Ensure a database named `objectStore` exists and is accessible.

3. (Optional) Run SQL migrations in `migrations/` using Flyway CLI or add Flyway to the project. Migrations are shipped under `migrations/` (V001, V002). NOTE: Flyway is not automatically configured in this project; run migrations manually or add `org.flywaydb:flyway-core` to POM.

4. Build and run:

```bash
mvn -DskipTests package
mvn spring-boot:run
# or: java -jar target/object_store-0.0.1-SNAPSHOT.jar
```

5. App should be available at `http://localhost:8080`.

---

## API Endpoints 📡

- GET / → Health check (returns `healthy`)
- GET /health → Health check (returns `ok`)
- GET /buckets → List buckets (returns JSON array)
- POST /buckets → Create a bucket
  - Body: `{"name": "my-bucket", "region": "localhost"}`
  - Returns 201 and created bucket DTO
- POST /{bucket}/{key} → Upload object
  - Multipart form: `file` (e.g. `curl -X POST -F "file=@file.txt" http://localhost:8080/my-bucket/file.txt`)
  - Returns 201 and object metadata
- GET /{bucket}/{key} → Download object
  - Returns file data and header `X-Checksum-SHA256`

Example: create bucket

```bash
curl -s -X POST -H "Content-Type: application/json" \
  -d '{"name":"my-bucket","region":"localhost"}' \
  http://localhost:8080/buckets
```

Upload a file

```bash
curl -s -X POST -F "file=@/path/to/local.file" http://localhost:8080/my-bucket/my-file
```

Download a file

```bash
curl -O http://localhost:8080/my-bucket/my-file
```

---

## Configuration & storage 🔧

- Upload directory currently configured in `ObjectStoreConfig` as `D:\object_store_data` (Windows path). You can change this in code or replace with a configurable property (recommended).
- Database connection via `application.properties`.
- Logging: the code contains a few `System.out` calls (development remnants) and minimal logging. Consider switching fully to SLF4J/Logback for production.

---

## Tests 🧪

- There is a single context-load test at `src/test`.
- Recommended: add unit tests for services and integration tests for controllers/file upload flows.

---

## Known issues & roadmap ⚠️

- Some debug `System.out.println` left in services — replace with structured logging.
- Exception handling is inconsistent (some endpoints throw `RuntimeException`, wrong exception types used). Add `@ControllerAdvice` to map exceptions to proper HTTP status codes.
- Database uniqueness / concurrency: ensure database enforces unique (bucket_id, object_key). Current migration creates an index — make it a UNIQUE constraint to prevent duplicates under concurrency and map DB constraint violations to 409 Conflict.
- `spring.jpa.hibernate.ddl-auto=update` is enabled; for production prefer `validate` or `none` and rely on migrations.
- Storage is local disk only; consider pluggable storage (S3/GCS/Blob) for HA/scale.

## Major gaps
Logging vs. debug prints (High)

Files: RegionService.java, BucketServiceImp.java use System.out.println.
Impact: noisy, uncontrolled logs; hard to manage in production.
Fix: replace with SLF4J/Logback logging, structured logs.
Improper exception types + missing global error mapping (High)

Examples: EntityExistsException for "not found", RuntimeException for IO errors.
Impact: wrong HTTP statuses and unclear API semantics.
Fix: introduce domain exceptions (e.g., NotFoundException, ConflictException) and a @ControllerAdvice mapping.
Race conditions & DB uniqueness handling (High)

Example: app-level existsBy... then insert; migration creates non-unique index.
Impact: duplicates on concurrent requests.
Fix: add DB-level UNIQUE constraint/index on (bucket_id, object_key) and map constraint violation → 409 Conflict.
Hardcoded filesystem path & config handling (High / Medium)

File: ObjectStoreConfig uses Paths.get("D:\\object_store_data").
Impact: non-portable, hard-to-change, fails CI/containers.
Fix: use @Value/@ConfigurationProperties, @PostConstruct to create dirs, and make storage pluggable (interface for S3/etc.).
Tight coupling & field injection (Medium)

Files: ObjectCoreController and services depend on concrete *Imp classes and use @Autowired field injection.
Impact: harder to test and swap implementations.
Fix: prefer constructor injection and program to interfaces.
Native queries & repository misuse (Medium)

BucketRepository uses native INSERT and SELECT with @Modifying.
Impact: error-prone; bypasses JPA semantics and lifecycle.
Fix: use JPA save() and repository query methods returning Optional<T>.
Entities & Lombok consistency / mutability (Low)

Mixed explicit getters + Lombok + setters; createdAt is inconsistently created.
Fix: pick a policy (immutable vs. mutable), remove duplicates, use lifecycle callbacks or set createdAt in services.
Test coverage is minimal (High)

Only a context-load test exists.
Fix: add unit tests for services, integration tests for controllers, and repository integration tests.

---

## Development & contribution 💡

- Fork and open a PR. Keep changes small and focused (e.g., logging cleanup, exception mapping, unique migration, tests).
- Please add tests for any bugfix or new feature.

---


