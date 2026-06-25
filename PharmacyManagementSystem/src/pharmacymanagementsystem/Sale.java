public class Sale implements Manageable {

    private String saleId;
    private String customerName;
    private String medicineName;
    private int quantitySold;
    private double totalAmount;
    private String paymentMethod;

    public Sale(String saleId, String customerName, String medicineName,
                int quantitySold, double totalAmount, String paymentMethod) {
        this.saleId = saleId;
        this.customerName = customerName;
        this.medicineName = medicineName;
        this.quantitySold = quantitySold;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }

   
    public String getSummary() {
        return "Sale ID: " + saleId
                + " | Customer: " + customerName
                + " | Medicine: " + medicineName
                + " | Qty: " + quantitySold
                + " | Total: $" + String.format("%.2f", totalAmount)
                + " | Payment: " + paymentMethod;
    }

   
    public boolean isValid() {
        return saleId != null && !saleId.isEmpty()
                && medicineName != null && !medicineName.isEmpty()
                && quantitySold > 0;
    }

    public String getSaleId() { return saleId; }
    public String getCustomerName() { return customerName; }
    public String getMedicineName() { return medicineName; }
    public int getQuantitySold() { return quantitySold; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
}
