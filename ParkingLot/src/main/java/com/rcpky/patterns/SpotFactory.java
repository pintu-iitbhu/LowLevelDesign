package com.rcpky.patterns;

import com.rcpky.enums.ParkingSpotType;
import com.rcpky.spots.*;

/**
 * Factory Pattern: For creating different types of parking spots
 */
public class SpotFactory {
    private static int spotIdCounter = 0;
    
    public static ParkingSpot createSpot(ParkingSpotType spotType) {
        spotIdCounter++;
        
        switch (spotType) {
            case HANDICAPPED:
                return new Handicapped(spotIdCounter);
            case COMPACT:
                return new Compact(spotIdCounter);
            case LARGE:
                return new Large(spotIdCounter);
            case MOTORCYCLE:
                return new MotorcycleSpot(spotIdCounter);
            default:
                throw new IllegalArgumentException("Unknown parking spot type: " + spotType);
        }
    }
}
