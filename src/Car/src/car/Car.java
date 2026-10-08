/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package car;

public class Car {
    String plateNumber;
    String model;
    double mileage;
    double fuelLevel;
    double tankCapacity;

    public Car(String plateNumber, String model, double fuelLevel, double tankCapacity) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.mileage = 0.0;
        this.fuelLevel = fuelLevel;
        this.tankCapacity = tankCapacity;
    }

    public void drive(double km) {
        double requiredFuel = km / 10.0;
        if (fuelLevel >= requiredFuel) {
            mileage += km;
            fuelLevel -= requiredFuel;
            System.out.println("Driving " + km + " km...");
        } else {
            System.out.println("Not enough fuel for this trip!");
        }
    }

    public void refuel(double amount) {
        System.out.println("Refueling " + amount + " liters...");
        if (fuelLevel + amount > tankCapacity) {
            fuelLevel = tankCapacity;
            System.out.println("Tank is full, extra fuel discarded.");
        } else {
            fuelLevel += amount;
        }
    }

    public void checkStatus() {
        System.out.println("Current mileage: " + mileage + " km, Fuel level: " + fuelLevel + " liters.");
        if (fuelLevel < 0.10 * tankCapacity) {
            System.out.println("Low fuel warning!");
        }
    }

    public static void main(String[] args) {
        Car myCar = new Car("34ABC123", "Sedan", 5.0, 50.0);
        
        myCar.checkStatus();
        myCar.drive(120);
        myCar.refuel(20);
        myCar.checkStatus();
        
        myCar.drive(1000);
        myCar.refuel(60);
    }
}
