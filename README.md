<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.1.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/JUnit%205-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5"/>
  <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="MIT License"/>
</p>

# ✈️ Spring Boot Tips — Strategy Pattern with Dependency Injection

> **A clean, production-grade example of the Strategy design pattern leveraging Spring Boot's auto-wiring to build an extensible validation pipeline for a flight reservation system.**

---

## 💡 Why This Project?

In real-world applications, validation logic tends to grow into an unmaintainable monolith. This project demonstrates how to **decompose business rules** into independent, testable components using *interface-based polymorphism* and *Spring's IoC container* — a pattern that scales from startups to enterprise.

### Key Takeaways

| Concept | Implementation |
|---|---|
| **Strategy Pattern** | `FlightReservationValidator` interface with 4 concrete validators |
| **Constructor Injection** | `List<FlightReservationValidator>` auto-collected by Spring |
| **Open/Closed Principle** | Add new validators without modifying existing code |
| **Custom Exceptions** | Domain-specific exception hierarchy for precise error handling |
| **Fluent API** | Builder-style setters on `ReservationOrder` for readable test setup |
| **Parameterized Tests** | `@ValueSource`, `@MethodSource` for exhaustive edge-case coverage |
| **Integration Testing** | `@SpringBootTest` verifying real DI wiring with AssertJ assertions |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    FlightReservationService                      │
│                                                                 │
│   processReservation(order)                                     │
│       └── validators.forEach(v -> v.validate(order))            │
└──────────────────────────┬──────────────────────────────────────┘
                           │
               ┌───────────┴───────────┐
               │  List<FlightReserva-  │
               │  tionValidator>       │
               │  (auto-injected)      │
               └───────────┬───────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
┌───────────────┐ ┌───────────────┐ ┌───────────────┐ ┌───────────────┐
│  Reference    │ │  UserInfo     │ │  Formula      │ │  NumberOfSeats│
│  Validator    │ │  Validator    │ │  Validator    │ │  Validator    │
│               │ │               │ │               │ │               │
│ • UUID format │ │ • Non-blank   │ │ • Privilege   │ │ • Seats > 0   │
│ • Length = 36 │ │ • Alpha-only  │ │   vs. Type    │ │               │
└───────────────┘ └───────────────┘ └───────────────┘ └───────────────┘
        │                  │                  │                  │
        ▼                  ▼                  ▼                  ▼
  ReservationRef     ReservationUser   ReservationFormula  ReservationNumber
  ValidationExc.     InfoValidExc.     ValidationExc.      OfSeatsValExc.
```

### 🔑 The Core Trick

```java
@Service
public class FlightReservationService {

    private final List<FlightReservationValidator> flightReservationValidators;

    // Spring auto-discovers ALL @Component implementations of the interface
    // and injects them as a list — no manual wiring, no configuration class needed
    public FlightReservationService(List<FlightReservationValidator> validators) {
        this.flightReservationValidators = validators;
    }

    public void processReservation(ReservationOrder order) {
        flightReservationValidators.forEach(v -> v.validate(order));
    }
}
```

> **💬 Why it matters:** Adding a new business rule is as simple as creating a new `@Component` that implements `FlightReservationValidator`. Zero changes to existing code. Zero risk of regression.

---

## 📂 Project Structure

```
src/
├── main/java/com/xeon/SpringBootTips/
│   ├── SpringBootTipsApplication.java
│   ├── reservation/
│   │   ├── FlightReservationService.java          ← Orchestrator (Strategy context)
│   │   ├── model/
│   │   │   ├── ReservationOrder.java               ← Domain model (fluent API)
│   │   │   ├── ReservationPrivilege.java            ← NONE | BUSINESS
│   │   │   └── ReservationType.java                 ← REGULAR | LAST_MINUTE
│   │   ├── validators/
│   │   │   ├── FlightReservationValidator.java      ← Strategy interface
│   │   │   ├── ReservationReferenceValidator.java   ← UUID format & length
│   │   │   ├── ReservationUserInfoValidator.java    ← Name validation
│   │   │   ├── ReservationFormulaValidator.java     ← Business rule enforcement
│   │   │   └── ReservationNumberOfSeatsValidator.java ← Capacity check
│   │   └── exceptions/
│   │       ├── ReservationReferenceValidationException.java
│   │       ├── ReservationUserInfoValidationException.java
│   │       ├── ReservationFormulaValidationException.java
│   │       └── ReservationNumberOfSeatsValidationException.java
│   └── utils/
│       └── StringHelper.java                        ← String utilities
│
└── test/java/com/xeon/SpringBootTips/
    └── reservation/
        └── FlightReservationServiceIT.java          ← Integration tests (7 test cases)
```

---

## 🧪 Testing Strategy

The project includes a comprehensive **integration test suite** (`FlightReservationServiceIT`) that boots the full Spring context and validates the autowired validator pipeline:

```java
@SpringBootTest
public class FlightReservationServiceIT {

    @Autowired
    private FlightReservationService flightReservationService;

    // Parameterized tests covering edge cases for each validator
    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "aaaa", "", "    "})
    void shouldThrowWhenUuidIsInvalid(String invalidUUID) { ... }

    // Method source for complex argument combinations
    @ParameterizedTest
    @MethodSource("getValidReservationTypeVersusPrivilegeCouples")
    void shouldAcceptValidFormulaCombinations(...) { ... }
}
```

| Test Technique | Purpose |
|---|---|
| `@SpringBootTest` | Full context integration — validates real DI wiring |
| `@ParameterizedTest` + `@ValueSource` | Boundary & edge-case coverage with minimal code |
| `@ParameterizedTest` + `@MethodSource` | Complex argument combinations for formula rules |
| AssertJ `assertThatThrownBy` | Fluent, readable exception assertions |
| AssertJ `assertThatCode` | Verifying happy paths don't throw |

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+**
- **Maven 3.8+** (or use the included Maven Wrapper)

### Run

```bash
# Clone the repository
git clone https://github.com/ValentinDumas/SpringBootTips.git
cd SpringBootTips

# Run all tests
./mvnw test

# Build the project
./mvnw clean package
```

---

## 🎯 Skills Demonstrated

- **Design Patterns** — Strategy pattern applied to a concrete business problem
- **SOLID Principles** — Open/Closed, Single Responsibility, Dependency Inversion
- **Spring Boot IoC** — Auto-collection of interface implementations via constructor injection
- **Domain Modeling** — Clean separation of models, validators, and exceptions
- **Test-Driven Development** — Comprehensive integration tests with parameterized inputs
- **Clean Code** — Expressive naming, fluent APIs, clear package structure

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  <sub>Built with ☕ and a passion for clean architecture</sub>
</p>
