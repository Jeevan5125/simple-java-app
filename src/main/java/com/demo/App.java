package com.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PricingService {

    private static final BigDecimal BASE_FARE = new BigDecimal("5.00");
    private static final BigDecimal PER_KM_RATE = new BigDecimal("1.50");
    private static final BigDecimal PER_MINUTE_RATE = new BigDecimal("0.50");
    private static final BigDecimal MAX_SURGE = new BigDecimal("5.0");

    public BigDecimal calculateFare(double distanceKm, int durationMin, int demand, int supply) {
        // Calculate Base Fare: Base + (Dist * Rate) + (Time * Rate)
        BigDecimal distanceCost = PER_KM_RATE.multiply(BigDecimal.valueOf(distanceKm));
        BigDecimal timeCost = PER_MINUTE_RATE.multiply(BigDecimal.valueOf(durationMin));
        BigDecimal subTotal = BASE_FARE.add(distanceCost).add(timeCost);

        // Calculate Surge Multiplier
        BigDecimal multiplier = calculateSurgeMultiplier(demand, supply);

        // Final Total
        return subTotal.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSurgeMultiplier(int demand, int supply) {
        if (supply == 0) return MAX_SURGE; // Avoid division by zero
        
        double ratio = (double) demand / supply;
        
        if (ratio <= 1.0) {
            return BigDecimal.valueOf(1.0); // Non-peak
        } else {
            // Formula: 1 + (Ratio - 1) * GrowthFactor
            double surgeValue = 1.0 + (ratio - 1.0) * 0.5;
            return BigDecimal.valueOf(Math.min(surgeValue, MAX_SURGE.doubleValue()));
        }
    }
}
