public class Supplier implements Manageable {

    private String supplierId;
    private String supplierName;
    private String contact;
    private String medicineSupplied;

    public Supplier(String supplierId, String supplierName, String contact, String medicineSupplied) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contact = contact;
        this.medicineSupplied = medicineSupplied;
    }


    public String getSummary() {
        return "Supplier: " + supplierName
                + " | Contact: " + contact
                + " | Supplies: " + medicineSupplied;
    }


    public boolean isValid() {
        return supplierId != null && !supplierId.isEmpty()
                && supplierName != null && !supplierName.isEmpty();
    }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getMedicineSupplied() { return medicineSupplied; }
    public void setMedicineSupplied(String medicineSupplied) { this.medicineSupplied = medicineSupplied; }
}
