/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ışıközturklab2;
import java.util.Scanner;
import java.util.ArrayList;

public class IşıkÖzturkLab2 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        ArrayList<String> stopNames = new ArrayList<>();
        ArrayList<Integer> boardingPassengers = new ArrayList<>();
        ArrayList<Integer> alightPassengers = new ArrayList<>();
        ArrayList<Integer> currentPassengersList = new ArrayList<>();

        int currentPassengers = 0;
        int max = 0;
        int count = 0;
        int overofcapacity = 0;

        System.out.println("Enter capacity:");
        int capacity = input.nextInt();
        
        System.out.println("Enter stop numbers:");
        int numberOfStops = input.nextInt();
        input.nextLine(); // Buffer temizleme

        for (int i = 0; i < numberOfStops; i++) {
            System.out.println("----------------------------------------");
            System.out.println((i + 1) + ". Stop Information");

            System.out.print("Stop Name: ");
            String stopName = input.nextLine();
            stopNames.add(stopName);

            System.out.print("Passengers boarding: ");
            int numberOfBoarding = input.nextInt();
            boardingPassengers.add(numberOfBoarding);

            System.out.print("Passengers alighting: ");
            int numberOfAlighting = input.nextInt();
            
            // İnnen yolcu mevcut yolcudan fazla olamaz kontrolü
            while (currentPassengers < numberOfAlighting) {
                System.out.println("----------------------------------------");
                System.out.println("Data error at [" + stopName + "]: cannot have more passengers alighting than are currently on the bus. Occupancy set to 0.");
                System.out.print("Please enter passengers alighting again: ");
                numberOfAlighting = input.nextInt();
                System.out.println("----------------------------------------");
            }
            
            alightPassengers.add(numberOfAlighting);

            // Mevcut yolcu sayısını güncelleme
            currentPassengers = currentPassengers + numberOfBoarding - numberOfAlighting;
            currentPassengersList.add(currentPassengers);

            System.out.println("After " + stopName + ", number of current passengers: " + currentPassengers);

            // Kapasite kontrolü
            if (currentPassengers > capacity) {
                System.out.println("----------------------------------------");
                System.out.println("Warning: Bus is over capacity at [" + stopName + "]");
                System.out.println("Number of passengers: " + currentPassengers + ", capacity: " + capacity);
                overofcapacity++;
                System.out.println("----------------------------------------");
            }

            // En çok binen yolcu (busiest stop) kontrolü
            if (numberOfBoarding > max) {
                max = numberOfBoarding;
                count = i;
            }

            input.nextLine(); // Buffer temizleme
        }

        // Tüm durakları listeleme
        System.out.println("\n--- Trip Summary ---");
        for (int i = 0; i < numberOfStops; i++) {
            System.out.println("----------------------------------------");
            System.out.println("Stop name: " + stopNames.get(i));
            System.out.println("Number of boarding passengers: " + boardingPassengers.get(i));
            System.out.println("Number of alighting passengers: " + alightPassengers.get(i));
            System.out.println("Current occupancy: " + currentPassengersList.get(i));
            System.out.println("----------------------------------------");
        }

        // İstatistikler
        System.out.println("Name of stop boarding max passengers: " + stopNames.get(count));
        
        int totalPassengersAcrossStops = 0;
        for (int p : currentPassengersList) {
            totalPassengersAcrossStops += p;
        }
        double average = (double) totalPassengersAcrossStops / numberOfStops;
        System.out.println("Average occupancy: " + average);
        System.out.println("Number of over capacity stops: " + overofcapacity);

        // Son durak kontrolü (Final Occupancy)
        if (currentPassengers != 0) {
            System.out.println("Warning: " + currentPassengers + " passengers still on the bus after the final stop - please check your data.");
        }
    }}
