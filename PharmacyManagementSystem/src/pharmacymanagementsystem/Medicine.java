import java.time.LocalDate;

public class Medicine extends Product {

    private int quantity;
    private LocalDate expiryDate;
    private String category;

    public Medicine(String id, String name, double price, int quantity, LocalDate expiryDate, String category) {
        super(id, name, price);
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.category = category;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getSummary() {
        return "Medicine: " + getName()
                + " | Category: " + category
                + " | Price: $" + String.format("%.2f", getPrice())
                + " | Qty: " + quantity
                + " | Expiry: " + expiryDate;
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    public boolean isExpiringSoon() {
        return !isExpired() && expiryDate.isBefore(LocalDate.now().plusDays(30));
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public void setCategory(String category) { this.category = category; }
}
