# Elevator System - Low-Level Design

A comprehensive object-oriented elevator system implementation, aligned with SDE2-level expectations, demonstrating multiple design patterns and clean architecture. The system supports multiple elevators and floors, simulates movement and door operations, and provides pluggable scheduling strategies.

## Project Overview

This project models a realistic elevator system that handles:
- Multiple elevators managed centrally by `ElevatorManager`
- A building with multiple floors managed by `FloorManager`
- External (hall) calls with direction intent and internal (car) floor selections
- Pluggable scheduling strategies to choose the best elevator for a request
- Door behavior with obstacle sensing and event notifications
- Thread-based simulation of movement, stops, and door operations

## Design Patterns Implemented

1. **Singleton Pattern**
   - `ElevatorSystem` – Single orchestrator coordinating elevators, floors, and scheduling

2. **Strategy Pattern**
   - `ElevatorScheduler` – Abstraction for scheduling algorithms
   - Concrete examples: `FirstComeFirstServeScheduler`, `NearestCarScheduler`

3. **Observer Pattern**
   - `ElevatorCar` (subject) notifies observers
   - `FloorManager` observes arrivals and door events to reset hall lights/update displays

4. **State Pattern**
   - `ElevatorContext` with states: `IdleState`, `MovingUpState`, `MovingDownState`
   - Logging-oriented states; motion logic orchestrated in `ElevatorCar`

## Project Structure

```
com.rcpky
├── ElevatorSystem.java                 # System singleton & demo main
├── Door.java                           # Door with sensor and state transitions
├── button
│   ├── Button.java
│   ├── ButtonFactory.java
│   ├── DoorButton.java
│   ├── ElevatorButton.java
│   ├── EmergencyButton.java
│   ├── FanButton.java
│   └── HallButton.java
├── display
│   └── Display.java
├── elevator
│   ├── ElevatorCar.java                # Runnable elevator with queues & events
│   └── ElevatorManager.java            # Fleet management and maintenance
├── enums
│   ├── ButtonType.java
│   ├── Direction.java
│   ├── DoorState.java
│   └── ElevatorState.java
├── floors
│   ├── Floor.java
│   └── FloorManager.java
├── observer
│   ├── ElevatorObserver.java
│   └── ElevatorSubject.java
├── panels
│   ├── ElevatorPanel.java
│   └── HallPanel.java
├── state
│   ├── ElevatorContext.java
│   ├── ElevatorState.java
│   ├── IdleState.java
│   ├── MaintenanceState.java
│   ├── MovingDownState.java
│   └── MovingUpState.java
└── strategy
    ├── ElevatorScheduler.java
    ├── FirstComeFirstServeScheduler.java
    └── NearestCarScheduler.java
```

## Key Implementations

### 1. Threading & Simulation
- Each `ElevatorCar` runs as a `Thread` via `Runnable` loop
- Simulates processing time (movement, door operations) using `Thread.sleep`
- Up/Down priority queues ensure ordered servicing in the current direction
- Maintenance mode clears queues and prevents movement

### 2. SOLID Principles
- **Single Responsibility**: Distinct classes for floors, cars, panels, buttons, scheduler
- **Open-Closed**: New schedulers or states can be added without modifying core classes
- **Liskov Substitution**: Interfaces/abstractions for observers and schedulers
- **Interface Segregation**: Focused interfaces (`ElevatorObserver`, `ElevatorScheduler`)
- **Dependency Inversion**: `ElevatorSystem` depends on abstractions (scheduler interface)

### 3. Error Handling
- Validation for elevator IDs and floor ranges
- Defensive checks for system running state
- Safe interruption handling in elevator threads
- Clear logging for invalid requests and maintenance constraints

## How to Run the Demo

Requirements:
- Java 21
- Maven (or Maven Wrapper)

Build from the repository root:
```
./mvnw -q -DskipTests package
```

Run the demo `main` (IDE recommended) at `com.rcpky.ElevatorSystem`. Sample flow in code:

```java
public static void main(String[] args) {
    // Create an elevator system with 3 elevators and 10 floors
    ElevatorSystem system = ElevatorSystem.getInstance(3, 10);

    // Start the system
    system.start();

    try {
        // External requests
        system.processExternalRequest(5, Direction.UP);
        Thread.sleep(1000);
        system.processExternalRequest(2, Direction.DOWN);

        // Internal requests
        system.processInternalRequest(0, 7);
        Thread.sleep(1000);
        system.processInternalRequest(1, 3);

        // Switch strategy at runtime
        system.setScheduler(new NearestCarScheduler());
        system.processExternalRequest(8, Direction.DOWN);

        Thread.sleep(10000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    } finally {
        system.stop();
    }
}
```

Optional CLI (if Maven Exec Plugin is configured):
```
./mvnw -pl ElevatorSystem -am exec:java -Dexec.mainClass="com.rcpky.ElevatorSystem"
```

## SDE2 Interview Relevance

Demonstrates key competencies:
1. **Object-Oriented Design** – Proper decomposition and abstractions
2. **Design Patterns** – Strategy, Observer, State, and Singleton in context
3. **Concurrency** – Runnable elevators with interruption handling
4. **Error Handling** – Validation and graceful degradation
5. **Code Organization** – Clean modular package structure

## Frequently Asked Questions

### How are requests scheduled among multiple elevators?
Requests are delegated to the strategy selected in `ElevatorSystem` via `ElevatorScheduler`. You can switch strategies at runtime to compare behavior (e.g., FCFS vs. Nearest Car).

### What happens when an elevator enters maintenance mode?
Its queues are cleared, direction set to IDLE, movement halted, and it’s excluded from scheduling until cleared.

### How does the system decide when to stop at a floor?
`ElevatorCar` checks its current direction queue for the current floor. Upon stopping, it opens/closes doors, resets button lights, notifies observers, and updates direction based on remaining requests.

## Relevant SDE2 Interview Questions

- High-level design: How would you model an elevator system for N elevators and M floors? Which components map to `ElevatorSystem`, `ElevatorManager`, `FloorManager`, and `ElevatorCar`?
- Scheduling trade-offs: Compare FCFS vs. Nearest Car. What metrics (wait time, throughput, fairness) inform `ElevatorScheduler` choice?
- Data structures: Why use two priority queues in `ElevatorCar` for UP/DOWN? What alternatives would you consider?
- Concurrency: How do you make the system thread-safe when multiple requests target the same elevator concurrently? Where would you add locks?
- Starvation & fairness: How do you prevent a car serving only UP requests from starving DOWN requests? Where to balance in `processMovement()`/direction updates?
- State machine: What transitions exist among `IdleState`, `MovingUpState`, `MovingDownState`? Would you push more logic into states vs. `ElevatorCar`?
- Error handling: How are invalid floor/elevator IDs handled in `ElevatorSystem` and `FloorManager`? What would you improve?
- Maintenance/emergency: How do `setMaintenance()` and `handleEmergency()` interact with queues and scheduling? What are the recovery steps?
- Door sensors: How would you avoid closing on obstacles (`Door.obstacleSensed()`), and what race conditions are possible?
- Load/capacity: Where would you incorporate `currentLoad`/capacity into scheduling decisions? What happens when capacity is exceeded mid-route?
- Scaling: How does complexity grow with elevators/floors/requests? Where are bottlenecks in `scheduler.scheduleElevator(...)`?
- Telemetry: What logs/metrics/alerts would you add to monitor SLA (average wait time, 95th percentile)?
- Testing: How would you unit test `ElevatorScheduler` and observer flows (`FloorManager`)? How to deterministically test timing-based behaviors?
- Failure recovery: If an elevator thread dies, how do you detect and respawn? Where should watchdogs live?
- Extensions: How would you implement destination dispatch or time-of-day heuristics? What API changes are needed?

## Future Enhancements

1. Add advanced schedulers (e.g., Look/SCAN, destination dispatch)
2. Improve state machine to drive transitions more explicitly
3. Introduce capacity/load-based decisions and energy optimization
4. Provide REST/GUI layer and real-time visualization
5. Persist events/metrics and add monitoring
6. Add unit tests for schedulers, state transitions, and observer flows
