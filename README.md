# Auron Risk Engine (Java)

Java service for the Auron collateral desk. It lists markets and positions, and checks whether a proposed borrow stays inside the market maximum LTV.

Java 17+. The JDK HTTP server is used, so there is no web framework.

## Run

```
mvn test
mvn -q compile exec:java
```

The server listens on port `8080` (`PORT` overrides it). `DeskBookTest` passes. `BorrowingLimitServiceTest` fails until the task below is implemented.

## Layout

- `com.auron.App` starts the server
- `com.auron.desk` holds markets, positions, and the sample book
- `com.auron.risk` is the borrow check
- `com.auron.http` is the JSON API

## API

- `GET /health`
- `GET /v1/markets` and `GET /v1/markets/{id}`
- `GET /v1/positions` and `GET /v1/positions/{id}`
- `POST /v1/borrow-check` with `collateral_value`, `existing_debt`, `additional_borrow`, `max_ltv_bps`
- `POST /v1/positions/{id}/borrow-check` with `additional_borrow`

## Candidate task: complete `BorrowingLimitService.evaluateBorrow`

Implement only `evaluateBorrow` in `src/main/java/com/auron/risk/BorrowingLimitService.java`. Return the existing `BorrowResult` record with `allowed`, `maxDebt`, and `totalDebt`. Throw `IllegalArgumentException` when `maxLtvBps` is above `10_000`.

1. `maxDebt = collateralValue * maxLtvBps / 10_000`, using integer division.
2. `totalDebt = existingDebt + additionalBorrow`.
3. `allowed = totalDebt <= maxDebt`. Borrowing exactly at the limit is allowed.
4. An LTV above `10_000` is invalid.

Assume supplied values are non-negative and that the multiplication and addition fit in a Java `long`. Do not change the record, the HTTP handlers, or the tests, and do not add dependencies. Only edit the named method.
