public abstract class Product implements Manageable {

    private String id;
    private String name;
    private double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public abstract String getCategory();

   
    public boolean isValid() { 
        return id != null && !id.isEmpty() && name != null && !name.isEmpty() && price >= 0;
    }

    
    public String getSummary() {
        return "Product: " + name + " | Category: " + getCategory() + " | Price: $" + String.format("%.2f", price);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
