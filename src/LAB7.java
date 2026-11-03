import java.util.ArrayList;
import java.util.List;

class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String message) {
        super(message);
    }
}

class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

class MinimumOrderException extends Exception {
    public MinimumOrderException(String message) {
        super(message);
    }
}

class MenuItem {
    private String name;
    private double price;
    private int quantity;

    public MenuItem(String name, double price, int quantity) throws InvalidQuantityException {
        this.name = name;
        setPrice(price);
        setQuantity(quantity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity cannot be 0 or negative.");
        }
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return name + " - Price: " + price + " TL - Qty: " + quantity + " - Total: " + getTotalPrice() + " TL";
    }
}

class Address {
    private String street;
    private String district;
    private String city;

    public Address(String street, String district, String city) {
        this.street = street;
        this.district = district;
        this.city = city;
    }

    public String getStreet() { return street; }
    public String getDistrict() { return district; }
    public String getCity() { return city; }

    @Override
    public String toString() {
        return street + ", " + district + "/" + city;
    }
}

class Customer {
    private String name;
    private String phone;
    private Address address;

    public Customer(String name, String phone, String street, String district, String city) {
        this.name = name;
        this.phone = phone;
        this.address = new Address(street, district, city);
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public Address getAddress() { return address; }

    @Override
    public String toString() {
        return "Customer: " + name + " | Phone: " + phone + " | Address: " + address;
    }
}

class Coupon {
    private String code;
    private double discountRate;
    private double minSubtotal;

    public Coupon(String code, double discountRate, double minSubtotal) {
        if (discountRate > 0.5) {
            discountRate = 0.5;
        }
        this.code = code;
        this.discountRate = discountRate;
        this.minSubtotal = minSubtotal;
    }

    public String getCode() { return code; }
    public double getDiscountRate() { return discountRate; }
    public double getMinSubtotal() { return minSubtotal; }
}

class Order {
    private static int nextOrderNumber = 1001;
    public static final double MIN_ORDER = 100.0;
    public static final double DELIVERY_FEE = 20.0;

    private static int completedOrders = 0;
    private static double totalRevenue = 0.0;

    private int orderNumber;
    private Customer customer;
    private ArrayList<MenuItem> items;
    private Coupon appliedCoupon;
    private boolean isCompleted;

    public Order(Customer customer) {
        this.orderNumber = nextOrderNumber++;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.isCompleted = false;
    }

    public void addItem(MenuItem newItem) throws InvalidQuantityException {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(newItem.getName())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }

    public void removeItem(String name) throws ItemNotFoundException {
        boolean removed = items.removeIf(item -> item.getName().equalsIgnoreCase(name));
        if (!removed) {
            throw new ItemNotFoundException("Item not found in order: " + name);
        }
    }

    public void updateQuantity(String name, int quantity) throws ItemNotFoundException, InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be positive.");
        }
        boolean found = false;
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                item.setQuantity(quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new ItemNotFoundException("Item not found in order: " + name);
        }
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (MenuItem item : items) {
            subtotal += item.getTotalPrice();
        }
        return subtotal;
    }

    public List<MenuItem> getItems() {
        List<MenuItem> copyList = new ArrayList<>();
        for (MenuItem item : items) {
            try {
                copyList.add(new MenuItem(item.getName(), item.getPrice(), item.getQuantity()));
            } catch (InvalidQuantityException e) {
            }
        }
        return copyList;
    }

    public void applyCoupon(Coupon coupon) {
        if (getSubtotal() >= coupon.getMinSubtotal()) {
            this.appliedCoupon = coupon;
            System.out.println("Kupon başarıyla uygulandı: " + coupon.getCode());
        } else {
            System.out.println("Kupon uygulanamadı! Sepet tutarı minimum kupon sınırının altında.");
        }
    }

    public void checkout() throws MinimumOrderException {
        if (isCompleted) {
            System.out.println("Bu sipariş zaten tamamlanmış!");
            return;
        }

        double subtotal = getSubtotal();
        if (subtotal < MIN_ORDER) {
            throw new MinimumOrderException("Sepet tutarı (" + subtotal + " TL) minimum sipariş tutarı olan " + MIN_ORDER + " TL'den düşük olamaz!");
        }

        double discountAmount = 0;
        if (appliedCoupon != null) {
            discountAmount = subtotal * appliedCoupon.getDiscountRate();
        }

        double finalTotal = (subtotal - discountAmount) + DELIVERY_FEE;

        System.out.println("\n--- SİPARİŞ FİŞİ ---");
        System.out.println("Sipariş No: " + orderNumber);
        System.out.println("Müşteri: " + customer.getName() + " | Adres: " + customer.getAddress());
        System.out.println("Ürünler:");
        for (MenuItem item : items) {
            System.out.println("  - " + item);
        }
        System.out.println("Ara Toplam: " + subtotal + " TL");
        if (appliedCoupon != null) {
            System.out.println("Kupon İndirimi (%" + (appliedCoupon.getDiscountRate() * 100) + "): -" + discountAmount + " TL");
        }
        System.out.println("Teslimat Ücreti: " + DELIVERY_FEE + " TL");
        System.out.println("Ödenecek Tutar: " + finalTotal + " TL");
        System.out.println("---------------------\n");

        completedOrders++;
        totalRevenue += finalTotal;
        isCompleted = true;
    }

    public static int getCompletedOrders() { return completedOrders; }
    public static double getTotalRevenue() { return totalRevenue; }
}

public class Main {
    public static void main(String[] args) {
        Customer c1 = new Customer("Ahmet Yılmaz", "5551234567", "Atatürk Cad. No:5", "Çankaya", "Ankara");
        Customer c2 = new Customer("Ayşe Demir", "5559876543", "Bağdat Cad. No:12", "Kadıköy", "İstanbul");

        Coupon validCoupon = new Coupon("INDIRIM20", 0.20, 150.0);
        Coupon invalidCoupon = new Coupon("DEVINDIRIM", 0.40, 1000.0);

        System.out.println("=== 1. SİPARİŞ TESTİ ===");
        Order order1 = new Order(c1);

        try {
            MenuItem m1 = new MenuItem("Burger", 70.0, 2);
            MenuItem m2 = new MenuItem("Patates Kızartması", 40.0, 1);

            order1.addItem(m1);
            order1.addItem(m2);
            order1.addItem(new MenuItem("Burger", 70.0, 1));

            order1.updateQuantity("Patates Kızartması", 2);
            order1.removeItem("Patates Kızartması");
            order1.addItem(new MenuItem("İçecek", 30.0, 2));

            order1.applyCoupon(validCoupon);
            order1.checkout();

        } catch (Exception e) {
            System.out.println("Hata yakalandı: " + e.getMessage());
        }

        System.out.println("=== 2. SİPARİŞ VE EXCEPTION TESTLERİ ===");
        Order order2 = new Order(c2);

        try {
            MenuItem invalidItem = new MenuItem("Çorba", 50.0, 0);
        } catch (InvalidQuantityException e) {
            System.out.println("[YAKALANDI] InvalidQuantityException: " + e.getMessage());
        }

        try {
            MenuItem m3 = new MenuItem("Su", 10.0, 2);
            order2.addItem(m3);
            order2.applyCoupon(invalidCoupon);
            order2.checkout();
        } catch (MinimumOrderException e) {
            System.out.println("[YAKALANDI] MinimumOrderException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Beklenmeyen hata: " + e.getMessage());
        }

        System.out.println("=== 3. EXCEPTION TESTİ: ItemNotFoundException ===");
        Order order3 = new Order(c1);
        try {
            order3.removeItem("OlmayanUrun");
        } catch (ItemNotFoundException e) {
            System.out.println("[YAKALANDI] ItemNotFoundException: " + e.getMessage());
        }

        System.out.println("\n=== SİSTEM İSTATİSTİKLERİ ===");
        System.out.println("Toplam Tamamlanan Sipariş Sayısı: " + Order.getCompletedOrders());
        System.out.println("Toplam Ciro (Revenue): " + Order.getTotalRevenue() + " TL");
    }
}