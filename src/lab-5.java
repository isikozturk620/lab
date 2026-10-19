class Package {
    private String senderName;
    private String senderAddress;
    private String senderCity;
    private String senderState;
    private String senderZip;
    private String recipientName;
    private String recipientAddress;
    private String recipientCity;
    private String recipientState;
    private String recipientZip;
    private double weight;
    private double costPerOunce;

    public Package(String senderName, String senderAddress, String senderCity, String senderState, String senderZip,
                   String recipientName, String recipientAddress, String recipientCity, String recipientState, String recipientZip,
                   double weight, double costPerOunce) {
        this.senderName = senderName;
        this.senderAddress = senderAddress;
        this.senderCity = senderCity;
        this.senderState = senderState;
        this.senderZip = senderZip;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.recipientCity = recipientCity;
        this.recipientState = recipientState;
        this.recipientZip = recipientZip;
        
        if (weight <= 0 || costPerOunce <= 0) {
            throw new IllegalArgumentException("Weight and cost per ounce must be positive.");
        }
        
        this.weight = weight;
        this.costPerOunce = costPerOunce;
    }

    public double getWeight() {
        return weight;
    }

    public double getCostPerOunce() {
        return costPerOunce;
    }

    public double calculateCost() {
        return weight * costPerOunce;
    }
}

class TwoDayPackage extends Package {
    private double flatFee;

    public TwoDayPackage(String senderName, String senderAddress, String senderCity, String senderState, String senderZip,
                         String recipientName, String recipientAddress, String recipientCity, String recipientState, String recipientZip,
                         double weight, double costPerOunce, double flatFee) {
        super(senderName, senderAddress, senderCity, senderState, senderZip,
              recipientName, recipientAddress, recipientCity, recipientState, recipientZip,
              weight, costPerOunce);
        this.flatFee = flatFee;
    }

    @Override
    public double calculateCost() {
        return super.calculateCost() + flatFee;
    }
}

class OvernightPackage extends Package {
    private double additionalFeePerOunce;

    public OvernightPackage(String senderName, String senderAddress, String senderCity, String senderState, String senderZip,
                            String recipientName, String recipientAddress, String recipientCity, String recipientState, String recipientZip,
                            double weight, double costPerOunce, double additionalFeePerOunce) {
        super(senderName, senderAddress, senderCity, senderState, senderZip,
              recipientName, recipientAddress, recipientCity, recipientState, recipientZip,
              weight, costPerOunce);
        this.additionalFeePerOunce = additionalFeePerOunce;
    }

    @Override
    public double calculateCost() {
        return super.calculateCost() + (getWeight() * additionalFeePerOunce);
    }
}

public class PackageTest {
    public static void main(String[] args) {
        Package pkg = new Package("Ali", "A", "Istanbul", "TR", "34", "Ayse", "B", "Ankara", "TR", "06", 10.0, 2.5);
        TwoDayPackage twoDay = new TwoDayPackage("Ali", "A", "Istanbul", "TR", "34", "Ayse", "B", "Ankara", "TR", "06", 8.0, 3.0, 5.0);
        OvernightPackage overnight = new OvernightPackage("Ali", "A", "Istanbul", "TR", "34", "Ayse", "B", "Ankara", "TR", "06", 5.0, 4.0, 1.5);

        System.out.println("Package: " + pkg.calculateCost());
        System.out.println("TwoDayPackage: " + twoDay.calculateCost());
        System.out.println("OvernightPackage: " + overnight.calculateCost());
    }
}