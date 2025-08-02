package com.rcpky;

import com.rcpky.enums.ParkingSpotType;
import com.rcpky.patterns.ParkingEventManager;
import com.rcpky.patterns.SpotFactory;
import com.rcpky.patterns.ThreadSafeParkingLot;
import com.rcpky.patterns.ThreadSafeParkingLot.ParkingException;
import com.rcpky.patterns.command.CommandInvoker;
import com.rcpky.patterns.command.ParkVehicleCommand;
import com.rcpky.patterns.observer.DisplayNotifier;
import com.rcpky.patterns.observer.SMSNotifier;
import com.rcpky.payments.CreditCardPayment;
import com.rcpky.payments.PaymentStrategy;
import com.rcpky.tickets.ParkingTicket;
import com.rcpky.vehicles.*;

import java.util.concurrent.*;

/**
 * Enhanced demonstration of Parking Lot design with multiple design patterns
 */
public class EnhancedParkingDemo {

    public void run() throws InterruptedException, ExecutionException {
        System.out.println("\n======== ENHANCED PARKING LOT DEMONSTRATION ========\n");
        
        // 1. Create thread-safe singleton parking lot
        ThreadSafeParkingLot parkingLot = ThreadSafeParkingLot.getInstance();
        
        // 2. Use factory pattern to create parking spots
        for (int i = 0; i < 3; i++) {
            parkingLot.addSpot(SpotFactory.createSpot(ParkingSpotType.COMPACT));
        }
        parkingLot.addSpot(SpotFactory.createSpot(ParkingSpotType.HANDICAPPED));
        parkingLot.addSpot(SpotFactory.createSpot(ParkingSpotType.LARGE));
        parkingLot.addSpot(SpotFactory.createSpot(ParkingSpotType.MOTORCYCLE));
        
        // 3. Set up observer pattern for notifications
        ParkingEventManager eventManager = new ParkingEventManager();
        SMSNotifier smsNotifier = new SMSNotifier();
        DisplayNotifier displayNotifier = new DisplayNotifier();
        eventManager.subscribe(smsNotifier);
        eventManager.subscribe(displayNotifier);
        
        // 4. Set up command pattern for operation history and undoing
        CommandInvoker commandInvoker = new CommandInvoker();
        
        // 5. Demonstrate concurrent operations with thread pool
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        System.out.println("\n--- DEMONSTRATION 1: Parking Vehicles Using Command Pattern ---\n");
        
        // Create vehicles
        Car car1 = new Car("KA-01-HH-1234");
        Car car2 = new Car("DL-12-AA-9999");
        Motorcycle bike = new Motorcycle("MH-04-XY-1111");
        
        // Create parking commands
        ParkVehicleCommand parkCar1 = new ParkVehicleCommand(parkingLot, car1);
        ParkVehicleCommand parkCar2 = new ParkVehicleCommand(parkingLot, car2);
        ParkVehicleCommand parkBike = new ParkVehicleCommand(parkingLot, bike);
        
        // Execute commands through invoker
        System.out.println("Parking car 1: " + (commandInvoker.executeCommand(parkCar1) ? "SUCCESS" : "FAILED"));
        System.out.println("Parking car 2: " + (commandInvoker.executeCommand(parkCar2) ? "SUCCESS" : "FAILED"));
        System.out.println("Parking bike: " + (commandInvoker.executeCommand(parkBike) ? "SUCCESS" : "FAILED"));
        
        System.out.println("\n--- DEMONSTRATION 2: Strategy Pattern for Payments ---\n");
        
        // Get car1's ticket
        ParkingTicket ticket = parkCar1.getTicket();
        if (ticket != null) {
            // Use credit card payment strategy
            PaymentStrategy creditCard = new CreditCardPayment(
                "4111111111111111", 
                "John Doe", 
                "123", 
                "12/25"
            );
            
            // Process payment using the strategy
            System.out.println("Processing payment for ticket #" + ticket.getTicketNo());
            boolean paymentSuccess = creditCard.processPayment(ticket, 15.00);
            System.out.println("Payment " + (paymentSuccess ? "successful" : "failed"));
        }
        
        System.out.println("\n--- DEMONSTRATION 3: Command Undo Operations ---\n");
        
        // Undo the last operation (parking the bike)
        System.out.println("Undoing last parking operation...");
        commandInvoker.undoLastCommand();
        
        // Check bike spot status after undo
        try {
            // Try to park the bike again - should succeed if undo worked
            parkingLot.parkVehicle(bike);
            System.out.println("Successfully re-parked bike after undo");
        } catch (ParkingException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        
        System.out.println("\n--- DEMONSTRATION 4: Concurrent Operations ---\n");
        
        // Create more vehicles for concurrent test
        Truck truck1 = new Truck("TN-04-CA-1234");
        Van van1 = new Van("KL-18-AB-5678");
        Car car3 = new Car("GJ-05-KL-9876");
        
        // Submit concurrent parking tasks
        Future<String> future1 = executor.submit(() -> parkVehicleSafely(parkingLot, truck1, "Thread-1"));
        Future<String> future2 = executor.submit(() -> parkVehicleSafely(parkingLot, van1, "Thread-2"));
        Future<String> future3 = executor.submit(() -> parkVehicleSafely(parkingLot, car3, "Thread-3"));
        
        // Get results
        System.out.println(future1.get());
        System.out.println(future2.get());
        System.out.println(future3.get());
        
        executor.shutdown();
        
        System.out.println("\n======== END OF ENHANCED DEMONSTRATION ========\n");
    }
    
    /**
     * Helper method for concurrent vehicle parking
     */
    private String parkVehicleSafely(ThreadSafeParkingLot parkingLot, Vehicle vehicle, String threadName) {
        try {
            ParkingTicket ticket = parkingLot.parkVehicle(vehicle);
            return threadName + ": Successfully parked " + vehicle.getClass().getSimpleName() + 
                   " " + vehicle.getLicenseNo() + " in spot " + ticket.getSlotNo();
        } catch (ParkingException e) {
            return threadName + ": Failed to park " + vehicle.getClass().getSimpleName() + 
                   " " + vehicle.getLicenseNo() + " - " + e.getMessage();
        }
    }
    
    /**
     * Main method to run the enhanced demo
     */
    public static void main(String[] args) {
        try {
            new EnhancedParkingDemo().run();
        } catch (Exception e) {
            System.err.println("Demo failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
