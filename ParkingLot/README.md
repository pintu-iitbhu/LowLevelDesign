# Parking Lot System - Low-Level Design

A comprehensive object-oriented design implementation for a parking lot system, incorporating multiple design patterns and best practices typically expected in SDE2-level technical interviews.

## Project Overview

This project demonstrates a robust, thread-safe parking lot management system that handles:
- Multiple vehicle types (Car, Motorcycle, Truck, Van)
- Different parking spot categories (Compact, Large, Handicapped, Motorcycle)
- Ticket generation and management
- Payment processing with various payment methods
- Real-time notifications and display updates

## Design Patterns Implemented

This implementation showcases several key design patterns:

1. **Singleton Pattern**
   - `ThreadSafeParkingLot` - Thread-safe implementation using double-checked locking

2. **Factory Pattern**
   - `SpotFactory` - Creates different types of parking spots

3. **Observer Pattern**
   - `NotificationService` - Notifies observers when parking spots are taken or freed
   - Concrete observers for SMS and display board updates

4. **Strategy Pattern**
   - `PaymentStrategy` - Different payment processing methods
   - Concrete implementations for credit card and cash payments

5. **Command Pattern**
   - `ParkingCommand` with concrete implementations
   - Command history with undo functionality
   - `CommandInvoker` for executing and tracking commands

## Project Structure

```
com.rcpky
├── enums
│   ├── AccountStatus.java
│   ├── PaymentStatus.java
│   ├── ParkingSpotType.java
│   └── TicketStatus.java
├── models
│   ├── Entrance.java
│   ├── Exit.java
│   └── ParkingRate.java
├── patterns
│   ├── command
│   │   ├── CommandInvoker.java
│   │   ├── FreeSpotCommand.java
│   │   ├── ParkingCommand.java
│   │   └── ParkVehicleCommand.java
│   ├── NotificationService.java
│   ├── SpotFactory.java
│   └── ThreadSafeParkingLot.java
├── payments
│   ├── CashPayment.java
│   ├── CreditCardPayment.java
│   ├── Payment.java
│   └── PaymentStrategy.java
├── spots
│   ├── Compact.java
│   ├── Handicapped.java
│   ├── Large.java
│   ├── MotorcycleSpot.java
│   ├── ParkingSpot.java
│   └── VehicleAccommodator.java
├── tickets
│   └── ParkingTicket.java
├── vehicles
│   ├── Car.java
│   ├── Motorcycle.java
│   ├── Truck.java
│   ├── Van.java
│   └── Vehicle.java
├── display
│   └── DisplayBoard.java
├── EnhancedParkingDemo.java
└── ParkingLot.java (original implementation)
```

## Key Implementations

### 1. Thread-Safety & Concurrency
- Double-checked locking for thread-safe singleton
- Concurrent collections (ConcurrentHashMap)
- Read-write locks for optimizing concurrent access
- Immutable objects where appropriate

### 2. SOLID Principles
- **Single Responsibility**: Each class has a well-defined responsibility
- **Open-Closed**: System can be extended with new vehicle types and spot types without modifying existing code
- **Liskov Substitution**: Vehicle and spot subclasses can be used interchangeably with their base classes
- **Interface Segregation**: Focused interfaces like `VehicleAccommodator` and `PaymentStrategy`
- **Dependency Inversion**: High-level modules depend on abstractions, not concrete implementations

### 3. Error Handling
- Custom exception types
- Graceful error recovery
- Proper exception propagation

## How to Run the Demo

The project includes an `EnhancedParkingDemo` class that demonstrates all the key features:

```java
public static void main(String[] args) {
    try {
        new EnhancedParkingDemo().run();
    } catch (Exception e) {
        System.err.println("Demo failed with error: " + e.getMessage());
        e.printStackTrace();
    }
}
```

This demo will showcase:
1. Creating parking spots using the factory pattern
2. Parking vehicles with the command pattern
3. Payment processing with the strategy pattern
4. Observer pattern notifications
5. Concurrent operations with thread safety
6. Command undo functionality

## SDE2 Interview Relevance

This implementation demonstrates key skills expected in SDE2-level interviews:

1. **Object-Oriented Design**: Proper use of inheritance, polymorphism, encapsulation, and abstraction
2. **Design Patterns**: Practical application of multiple design patterns
3. **Thread Safety**: Handling concurrent operations correctly
4. **Error Handling**: Robust exception handling
5. **SOLID Principles**: Adherence to good design principles
6. **Code Organization**: Clean, maintainable code structure

## Frequently Asked Questions

### Why are we using write locks in the ThreadSafeParkingLot implementation?

We're using write locks in the ThreadSafeParkingLot class for several important reasons:

1. **Mutual Exclusion during State Changes**: Write locks provide exclusive access when the state of the parking lot is being modified. This happens in two critical methods:
   - `parkVehicle()`: When a vehicle is being assigned to a spot
   - `freeSlot()`: When a spot is being freed up

2. **Consistency during Mutations**: When we modify multiple related data structures together, we need to ensure they remain consistent. For example, when parking a vehicle:
   - We update the spot's state (assign a vehicle)
   - We create and store a ticket
   - We notify observers
   - We update display boards

3. **Preventing Race Conditions**: Without write locks, concurrent operations could cause issues like:
   - Two threads trying to park different vehicles in the same spot
   - A thread reading spot status while another is modifying it
   - Inconsistency between spots and tickets collections

4. **Read-Write Lock Benefits**: We use ReadWriteLock (ReentrantReadWriteLock) instead of a simple mutex because:
   - It allows multiple concurrent readers (for methods like getAllSpots())
   - It blocks readers only when a write is in progress
   - It improves performance for read-heavy workloads

For read-only operations like `getAllSpots()`, we use the read lock, which allows multiple threads to read the state concurrently as long as no thread is modifying it.

This approach follows the principle of "lock when writing, share when reading," which is a common pattern in concurrent systems that need to maintain data consistency while maximizing throughput.

## Future Enhancements

Potential areas for extending the system:
1. Adding database persistence layer
2. Implementing a REST API
3. Adding authentication and authorization
4. Creating a UI layer
5. Implementing metrics and monitoring
6. Adding payment gateway integration
