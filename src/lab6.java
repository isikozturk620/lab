import java.util.ArrayList;

class Ride {
    private String passengerName;
    private String pickup;
    private String destination;
    private double distanceKm;
    private int durationMin;
    private double pricePerKm;

    public Ride(String passengerName, String pickup, String destination, double distanceKm, int durationMin, double pricePerKm) {
        this.passengerName = passengerName;
        this.pickup = pickup;
        this.destination = destination;
        if (distanceKm > 0) {
            this.distanceKm = distanceKm;
        } else {
            System.out.println("Warning: distanceKm must be positive. Set to 1.");
            this.distanceKm = 1;
        }
        if (durationMin > 0) {
            this.durationMin = durationMin;
        } else {
            System.out.println("Warning: durationMin must be positive. Set to 1.");
            this.durationMin = 1;
        }
        if (pricePerKm > 0) {
            this.pricePerKm = pricePerKm;
        } else {
            System.out.println("Warning: pricePerKm must be positive. Set to 1.");
            this.pricePerKm = 1;
        }
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getPickup() {
        return pickup;
    }

    public String getDestination() {
        return destination;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }

    public double calculateFare() {
        return distanceKm * pricePerKm;
    }

    public String toString() {
        return "Passenger: " + passengerName + ", Pickup: " + pickup + ", Destination: " + destination + ", Distance: " + distanceKm + " km, Duration: " + durationMin + " min";
    }
}

class EconomyRide extends Ride {
    private double pricePerMinute;

    public EconomyRide(String passengerName, String pickup, String destination, double distanceKm, int durationMin, double pricePerKm, double pricePerMinute) {
        super(passengerName, pickup, destination, distanceKm, durationMin, pricePerKm);
        if (pricePerMinute > 0) {
            this.pricePerMinute = pricePerMinute;
        } else {
            System.out.println("Warning: pricePerMinute must be positive. Set to 1.");
            this.pricePerMinute = 1;
        }
    }

    public double getPricePerMinute() {
        return pricePerMinute;
    }

    @Override
    public double calculateFare() {
        double fare = super.calculateFare() + (getDurationMin() * pricePerMinute);
        if (fare < 50) {
            return 50;
        }
        return fare;
    }

    @Override
    public String toString() {
        return super.toString() + ", Price Per Minute: " + pricePerMinute;
    }
}

class PremiumRide extends Ride {
    private double comfortFee;
    private double surgeMultiplier;

    public PremiumRide(String passengerName, String pickup, String destination, double distanceKm, int durationMin, double pricePerKm, double comfortFee, double surgeMultiplier) {
        super(passengerName, pickup, destination, distanceKm, durationMin, pricePerKm);
        this.comfortFee = comfortFee;
        if (surgeMultiplier >= 1.0 && surgeMultiplier <= 3.0) {
            this.surgeMultiplier = surgeMultiplier;
        } else {
            System.out.println("Warning: surgeMultiplier must be between 1.0 and 3.0. Set to 1.0.");
            this.surgeMultiplier = 1.0;
        }
    }

    public double getcomfortFee() {
        return comfortFee;
    }

    public double getSurgeMultiplier() {
        return surgeMultiplier;
    }

    @Override
    public double calculateFare() {
        return (super.calculateFare() + comfortFee) * surgeMultiplier;
    }

    @Override
    public String toString() {
        return super.toString() + ", Comfort Fee: " + comfortFee + ", Surge Multiplier: " + surgeMultiplier;
    }
}

class SharedRide extends EconomyRide {
    private int passengerCount;

    public SharedRide(String passengerName, String pickup, String destination, double distanceKm, int durationMin, double pricePerKm, double pricePerMinute, int passengerCount) {
        super(passengerName, pickup, destination, distanceKm, durationMin, pricePerKm, pricePerMinute);
        if (passengerCount >= 1 && passengerCount <= 4) {
            this.passengerCount = passengerCount;
        } else {
            System.out.println("Warning: passengerCount must be between 1 and 4. Set to 1.");
            this.passengerCount = 1;
        }
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    @Override
    public double calculateFare() {
        double baseEconomyFare = super.calculateFare();
        double totalSharedFare = (baseEconomyFare / passengerCount) * 1.10;
        return totalSharedFare;
    }

    @Override
    public String toString() {
        return super.toString() + ", Passenger Count: " + passengerCount;
    }
}

public class Main {
    public static void main(String[] args) {
        ArrayList<Ride> rides = new ArrayList<>();
        rides.add(new Ride("Ahmet", "Kadikoy", "Taksim", 10.0, 20, 5.0));
        rides.add(new EconomyRide("Mehmet", "Besiktas", "Uskudar", 8.0, 15, 4.0, 2.0));
        rides.add(new PremiumRide("Ayse", "Levent", "Maslak", 12.0, 25, 6.0, 50.0, 1.5));
        rides.add(new SharedRide("Fatma", "Maltepe", "Kartal", 15.0, 30, 4.0, 1.5, 2));

        for (Ride ride : rides) {
            System.out.println(ride.toString() + ", Fare: " + ride.calculateFare() + " TL");
        }
    }
}