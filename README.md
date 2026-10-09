# SmartPark

REST API for registering parking lots and vehicles, checking vehicles in and out, and viewing occupancy.

## AI usage

1. AI was used to generate the validation regex for license plates (`^[A-Za-z0-9-]+$`) and owner names (`^[A-Za-z]+(?: [A-Za-z]+)*$`).
2. AI assisted in making this README.

## Setup

- Java 17
- Spring Boot 4.1.1
- Maven Wrapper

Added from Spring Initializr:

- Spring Web (`spring-boot-starter-webmvc`, and `spring-boot-starter-webmvc-test`)
- Spring Data JPA (`spring-boot-starter-data-jpa`, and `spring-boot-starter-data-jpa-test`)
- H2 Database
- Lombok
- Validation (`spring-boot-starter-validation`) is also on the classpath so `@Valid` runs.

## Build and run

Build:

```bash
./mvnw clean package
```

Run:

```bash
./mvnw spring-boot:run
```

The app listens on `http://localhost:8080`.

H2 is in memory and starts empty. Register a lot and a vehicle before check-in. No dummy data is loaded.

## API

Import [smartpark.postman_collection.json](smartpark.postman_collection.json) into Postman. Requests use JSON.

| Method | Path | Body |
| --- | --- | --- |
| POST | `/parking-lot/register` | `lotId`, `location`, `capacity` |
| GET | `/parking-lot/{lotId}` | |
| GET | `/parking-lot/{lotId}/availability` | |
| GET | `/parking-lot/{lotId}/vehicles` | |
| POST | `/parking-lot/{lotId}/check-in` | `{ "licensePlate": "ABC-123" }` |
| POST | `/parking-lot/{lotId}/check-out` | `{ "licensePlate": "ABC-123" }` |
| POST | `/vehicle` | `licensePlate`, `ownerName`, `vehicleType` |
| GET | `/vehicle/{licensePlate}` | |

`vehicleType` is `CAR`, `MOTORCYCLE`, or `TRUCK`.

Failed field validation returns 400. A missing lot or vehicle returns 404. A duplicate lot id or license plate returns 409.

## Sample flow

1. Register two vehicles. Both return 201.

```bash
curl -X POST http://localhost:8080/vehicle \
  -H "Content-Type: application/json" \
  -d '{"licensePlate":"ABC-123","ownerName":"Ana Cruz","vehicleType":"CAR"}'

curl -X POST http://localhost:8080/vehicle \
  -H "Content-Type: application/json" \
  -d '{"licensePlate":"DEF-456","ownerName":"Ben Santos","vehicleType":"CAR"}'
```

2. Register two lots, each with one space. Both return 200.

```bash
curl -X POST http://localhost:8080/parking-lot/register \
  -H "Content-Type: application/json" \
  -d '{"lotId":"LOT_1A","location":"Main Street","capacity":1}'

curl -X POST http://localhost:8080/parking-lot/register \
  -H "Content-Type: application/json" \
  -d '{"lotId":"LOT_2B","location":"Second Avenue","capacity":1}'
```

3. Check `ABC-123` into `LOT_1A`. This returns 200 and `occupiedSpaces` is 1. Check `DEF-456` into the same lot. The lot is full:

```json
{ "lotId": "Parking lot is full: LOT_1A" }
```

Status is 404.

4. Check `ABC-123` into `LOT_2B`. The vehicle is already parked:

```json
{ "licensePlate": "Vehicle already parked on LOT_1A" }
```

Status is 404.

## Input validation

`@Valid` on the controller checks the request body before the service runs. A failed check throws `MethodArgumentNotValidException`. `GlobalExceptionHandler` returns 400 and a map of field name to message.

`POST /vehicle` rejects a blank license plate, a plate that is not letters, numbers, and hyphens, a blank owner name, an owner name that is not letters and single spaces, and a missing `vehicleType`.

`POST /parking-lot/register` rejects a blank lot id, a lot id longer than 50 characters, a blank location, a missing capacity, and a capacity below 0.

Check-in and check-out use the same license plate rule: the plate is required and must match `^[A-Za-z0-9-]+$`.

## Tests

Run the tests:

```bash
./mvnw test
```

`ParkingLotServiceTests` mocks `ParkingLotRepository` and `VehicleService`. It does not start Spring or H2.

The cases are:

- Reject check-in when the lot is full.
- Increase occupied spaces on check-in.
- Reject check-in when the vehicle is already parked.
- Decrease occupied spaces on check-out.
- Reject check-out when the vehicle is not parked.
- Reject check-out when the vehicle is parked in another lot.
- Reject a request for an unknown lot id.

`VehicleServiceTests` mocks `VehicleRepository` and rejects an unknown license plate.
