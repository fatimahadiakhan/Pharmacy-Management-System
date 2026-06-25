import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.function.Function;

public class Main extends Application {

    private ObservableList<Medicine> medicines = FXCollections.observableArrayList();
    private ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    private ObservableList<Sale> sales = FXCollections.observableArrayList();
    private ObservableList<Prescription> prescriptions = FXCollections.observableArrayList();

   
    public void start(Stage primaryStage) {
        VBox root = new VBox();

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2c3e50;");
        Label headerLabel = new Label("Pharmacy Management System");
        headerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        header.getChildren().add(headerLabel);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
            new Tab("Medicine Inventory", buildMedicineTab()),
            new Tab("Suppliers", buildSupplierTab()),
            new Tab("Sales", buildSalesTab()),
            new Tab("Prescriptions", buildPrescriptionTab()),
            new Tab("Expiry Alerts", buildExpiryTab())
        );

        VBox.setVgrow(tabPane, Priority.ALWAYS);
        root.getChildren().addAll(header, tabPane);

        Scene scene = new Scene(root, 950, 700);
        primaryStage.setTitle("Pharmacy Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox buildMedicineTab() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        Label title = new Label("Medicine Inventory");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        TextField idField = new TextField();    idField.setPromptText("e.g. M001");
        TextField nameField = new TextField();  nameField.setPromptText("e.g. Panadol");
        TextField priceField = new TextField(); priceField.setPromptText("e.g. 5.50");
        TextField qtyField = new TextField();   qtyField.setPromptText("e.g. 100");
        DatePicker expiryPicker = new DatePicker();

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Antibiotic", "Painkiller", "Antiviral", "Vitamin", "Antifungal", "Other");
        categoryCombo.setValue("Painkiller");

        form.add(new Label("ID:"), 0, 0);           form.add(idField, 1, 0);
        form.add(new Label("Name:"), 2, 0);         form.add(nameField, 3, 0);
        form.add(new Label("Price:"), 0, 1);        form.add(priceField, 1, 1);
        form.add(new Label("Quantity:"), 2, 1);     form.add(qtyField, 3, 1);
        form.add(new Label("Expiry Date:"), 0, 2);  form.add(expiryPicker, 1, 2);
        form.add(new Label("Category:"), 2, 2);     form.add(categoryCombo, 3, 2);

        ToggleGroup stockGroup = new ToggleGroup();
        RadioButton inStockBtn = new RadioButton("In Stock");
        RadioButton lowStockBtn = new RadioButton("Low Stock");
        inStockBtn.setToggleGroup(stockGroup);
        lowStockBtn.setToggleGroup(stockGroup);
        inStockBtn.setSelected(true);
        HBox radioBox = new HBox(10, new Label("Stock Status:"), inStockBtn, lowStockBtn);
        radioBox.setAlignment(Pos.CENTER_LEFT);

        TableView<Medicine> table = new TableView<>(medicines);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addColumn(table, "ID",          m -> m.getId());
        addColumn(table, "Name",        m -> m.getName());
        addColumn(table, "Price",       m -> String.format("$%.2f", m.getPrice()));
        addColumn(table, "Quantity",    m -> String.valueOf(m.getQuantity()));
        addColumn(table, "Expiry",      m -> m.getExpiryDate().toString());
        addColumn(table, "Category",    m -> m.getCategory());

        Button addBtn     = new Button("Add Medicine");
        Button deleteBtn  = new Button("Delete");
        Button summaryBtn = new Button("View Summary");
        Button clearBtn   = new Button("Clear");
        HBox btnBox = new HBox(10, addBtn, deleteBtn, summaryBtn, clearBtn);

        addBtn.setOnAction(e -> {
            try {
                if (idField.getText().isEmpty() || nameField.getText().isEmpty() || expiryPicker.getValue() == null) {
                    showAlert("Validation Error", "Please fill all required fields.");
                    return;
                }
                Medicine m = new Medicine(
                    idField.getText(),
                    nameField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(qtyField.getText()),
                    expiryPicker.getValue(),
                    categoryCombo.getValue()
                );
                medicines.add(m);
                idField.clear(); nameField.clear(); priceField.clear();
                qtyField.clear(); expiryPicker.setValue(null);
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Price and Quantity must be valid numbers.");
            }
        });

        deleteBtn.setOnAction(e -> {
            Medicine selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) medicines.remove(selected);
            else showAlert("Selection Error", "Please select a medicine to delete.");
        });

        summaryBtn.setOnAction(e -> {
            Medicine selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showAlert("Medicine Summary", selected.getSummary());
            else showAlert("Selection Error", "Please select a medicine first.");
        });

        clearBtn.setOnAction(e -> {
            idField.clear(); nameField.clear(); priceField.clear();
            qtyField.clear(); expiryPicker.setValue(null);
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        box.getChildren().addAll(title, form, radioBox, btnBox, table);
        return box;
    }

    private VBox buildSupplierTab() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        Label title = new Label("Supplier Management");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(8);

        TextField idField   = new TextField(); idField.setPromptText("e.g. S001");
        TextField nameField = new TextField(); nameField.setPromptText("e.g. MedCo Pharma");
        TextField contactField = new TextField(); contactField.setPromptText("e.g. 0321-1234567");
        TextField medField  = new TextField(); medField.setPromptText("e.g. Panadol, Aspirin");

        form.add(new Label("Supplier ID:"), 0, 0);      form.add(idField, 1, 0);
        form.add(new Label("Name:"), 2, 0);             form.add(nameField, 3, 0);
        form.add(new Label("Contact:"), 0, 1);          form.add(contactField, 1, 1);
        form.add(new Label("Medicines Supplied:"), 2, 1); form.add(medField, 3, 1);

        TableView<Supplier> table = new TableView<>(suppliers);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addColumn(table, "ID",                s -> s.getSupplierId());
        addColumn(table, "Name",              s -> s.getSupplierName());
        addColumn(table, "Contact",           s -> s.getContact());
        addColumn(table, "Medicines Supplied",s -> s.getMedicineSupplied());

        Button addBtn     = new Button("Add Supplier");
        Button deleteBtn  = new Button("Delete");
        Button summaryBtn = new Button("View Summary");
        HBox btnBox = new HBox(10, addBtn, deleteBtn, summaryBtn);

        addBtn.setOnAction(e -> {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty()) {
                showAlert("Validation Error", "ID and Name are required.");
                return;
            }
            Supplier s = new Supplier(idField.getText(), nameField.getText(), contactField.getText(), medField.getText());
            suppliers.add(s);
            idField.clear(); nameField.clear(); contactField.clear(); medField.clear();
        });

        deleteBtn.setOnAction(e -> {
            Supplier selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) suppliers.remove(selected);
            else showAlert("Selection Error", "Please select a supplier to delete.");
        });

        summaryBtn.setOnAction(e -> {
            Supplier selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showAlert("Supplier Summary", selected.getSummary());
            else showAlert("Selection Error", "Please select a supplier first.");
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        box.getChildren().addAll(title, form, btnBox, table);
        return box;
    }

    private VBox buildSalesTab() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        Label title = new Label("Sales Management");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(8);

        TextField saleIdField   = new TextField(); saleIdField.setPromptText("e.g. SL001");
        TextField customerField = new TextField(); customerField.setPromptText("e.g. Ali Hassan");
        ComboBox<String> medicineCombo = new ComboBox<>();
        medicineCombo.setPromptText("Select Medicine");
        TextField qtyField = new TextField(); qtyField.setPromptText("e.g. 2");

        Label totalLabel = new Label("Total: $0.00");
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        medicines.addListener((javafx.collections.ListChangeListener<Medicine>) c -> {
            medicineCombo.getItems().clear();
            for (Medicine m : medicines) medicineCombo.getItems().add(m.getName());
        });

        ToggleGroup paymentGroup = new ToggleGroup();
        RadioButton cashBtn = new RadioButton("Cash");
        RadioButton cardBtn = new RadioButton("Card");
        cashBtn.setToggleGroup(paymentGroup);
        cardBtn.setToggleGroup(paymentGroup);
        cashBtn.setSelected(true);
        HBox paymentBox = new HBox(10, new Label("Payment Method:"), cashBtn, cardBtn);
        paymentBox.setAlignment(Pos.CENTER_LEFT);

        form.add(new Label("Sale ID:"), 0, 0);    form.add(saleIdField, 1, 0);
        form.add(new Label("Customer:"), 2, 0);   form.add(customerField, 3, 0);
        form.add(new Label("Medicine:"), 0, 1);   form.add(medicineCombo, 1, 1);
        form.add(new Label("Quantity:"), 2, 1);   form.add(qtyField, 3, 1);
        form.add(totalLabel, 0, 2, 4, 1);

        qtyField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (medicineCombo.getValue() != null && !newVal.isEmpty()) {
                try {
                    int qty = Integer.parseInt(newVal);
                    medicines.stream()
                        .filter(m -> m.getName().equals(medicineCombo.getValue()))
                        .findFirst()
                        .ifPresent(m -> totalLabel.setText(String.format("Total: $%.2f", m.getPrice() * qty)));
                } catch (NumberFormatException ignored) {}
            }
        });

        TableView<Sale> table = new TableView<>(sales);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addColumn(table, "Sale ID",  s -> s.getSaleId());
        addColumn(table, "Customer", s -> s.getCustomerName());
        addColumn(table, "Medicine", s -> s.getMedicineName());
        addColumn(table, "Qty",      s -> String.valueOf(s.getQuantitySold()));
        addColumn(table, "Total",    s -> String.format("$%.2f", s.getTotalAmount()));
        addColumn(table, "Payment",  s -> s.getPaymentMethod());

        Button recordBtn  = new Button("Record Sale");
        Button deleteBtn  = new Button("Delete");
        Button summaryBtn = new Button("View Summary");
        HBox btnBox = new HBox(10, recordBtn, deleteBtn, summaryBtn);

        recordBtn.setOnAction(e -> {
            try {
                if (saleIdField.getText().isEmpty() || medicineCombo.getValue() == null || qtyField.getText().isEmpty()) {
                    showAlert("Validation Error", "Please fill all required fields.");
                    return;
                }
                int qty = Integer.parseInt(qtyField.getText());
                String medName = medicineCombo.getValue();
                Medicine found = medicines.stream()
                    .filter(m -> m.getName().equals(medName))
                    .findFirst().orElse(null);
                if (found == null) { showAlert("Error", "Medicine not found in inventory."); return; }
                if (found.getQuantity() < qty) {
                    showAlert("Stock Error", "Insufficient stock. Available: " + found.getQuantity());
                    return;
                }
                double total = found.getPrice() * qty;
                found.setQuantity(found.getQuantity() - qty);
                table.refresh();
                String payment = cashBtn.isSelected() ? "Cash" : "Card";
                Sale sale = new Sale(saleIdField.getText(), customerField.getText(), medName, qty, total, payment);
                sales.add(sale);
                saleIdField.clear(); customerField.clear(); qtyField.clear();
                totalLabel.setText("Total: $0.00");
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Quantity must be a valid number.");
            }
        });

        deleteBtn.setOnAction(e -> {
            Sale selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) sales.remove(selected);
            else showAlert("Selection Error", "Please select a sale to delete.");
        });

        summaryBtn.setOnAction(e -> {
            Sale selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showAlert("Sale Summary", selected.getSummary());
            else showAlert("Selection Error", "Please select a sale first.");
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        box.getChildren().addAll(title, form, paymentBox, btnBox, table);
        return box;
    }

    private VBox buildPrescriptionTab() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        Label title = new Label("Prescription Management");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(8);

        TextField idField      = new TextField(); idField.setPromptText("e.g. P001");
        TextField patientField = new TextField(); patientField.setPromptText("e.g. Ahmed Ali");
        TextField doctorField  = new TextField(); doctorField.setPromptText("e.g. Dr. Sara");
        TextField medsField    = new TextField(); medsField.setPromptText("e.g. Panadol, Amoxil");
        DatePicker datePicker  = new DatePicker();

        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton regularBtn = new RadioButton("Regular");
        RadioButton urgentBtn  = new RadioButton("Urgent");
        regularBtn.setToggleGroup(typeGroup);
        urgentBtn.setToggleGroup(typeGroup);
        regularBtn.setSelected(true);
        HBox typeBox = new HBox(10, new Label("Prescription Type:"), regularBtn, urgentBtn);
        typeBox.setAlignment(Pos.CENTER_LEFT);

        form.add(new Label("ID:"), 0, 0);       form.add(idField, 1, 0);
        form.add(new Label("Patient:"), 2, 0);  form.add(patientField, 3, 0);
        form.add(new Label("Doctor:"), 0, 1);   form.add(doctorField, 1, 1);
        form.add(new Label("Date:"), 2, 1);     form.add(datePicker, 3, 1);
        form.add(new Label("Medicines:"), 0, 2); form.add(medsField, 1, 2, 3, 1);

        TableView<Prescription> table = new TableView<>(prescriptions);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addColumn(table, "ID",       p -> p.getPrescriptionId());
        addColumn(table, "Patient",  p -> p.getPatientName());
        addColumn(table, "Doctor",   p -> p.getDoctorName());
        addColumn(table, "Medicines",p -> p.getMedicines());
        addColumn(table, "Type",     p -> p.getPrescriptionType());
        addColumn(table, "Date",     p -> p.getDate().toString());

        Button addBtn     = new Button("Add Prescription");
        Button deleteBtn  = new Button("Delete");
        Button summaryBtn = new Button("View Summary");
        HBox btnBox = new HBox(10, addBtn, deleteBtn, summaryBtn);

        addBtn.setOnAction(e -> {
            if (idField.getText().isEmpty() || patientField.getText().isEmpty() || datePicker.getValue() == null) {
                showAlert("Validation Error", "ID, Patient, and Date are required.");
                return;
            }
            String type = regularBtn.isSelected() ? "Regular" : "Urgent";
            Prescription p = new Prescription(
                idField.getText(), patientField.getText(), doctorField.getText(),
                medsField.getText(), datePicker.getValue(), type
            );
            prescriptions.add(p);
            idField.clear(); patientField.clear(); doctorField.clear();
            medsField.clear(); datePicker.setValue(null);
        });

        deleteBtn.setOnAction(e -> {
            Prescription selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) prescriptions.remove(selected);
            else showAlert("Selection Error", "Please select a prescription to delete.");
        });

        summaryBtn.setOnAction(e -> {
            Prescription selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showAlert("Prescription Summary", selected.getSummary());
            else showAlert("Selection Error", "Please select a prescription first.");
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        box.getChildren().addAll(title, form, typeBox, btnBox, table);
        return box;
    }

    private VBox buildExpiryTab() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));

        Label title = new Label("Expiry Notifications");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label info = new Label("Click 'Check Expiry' to see medicines that are expired or expiring within 30 days.");

        ObservableList<Medicine> expiryList = FXCollections.observableArrayList();

        TableView<Medicine> table = new TableView<>(expiryList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addColumn(table, "ID",          m -> m.getId());
        addColumn(table, "Name",        m -> m.getName());
        addColumn(table, "Expiry Date", m -> m.getExpiryDate().toString());
        addColumn(table, "Category",    m -> m.getCategory());
        addColumn(table, "Status",      m -> m.isExpired() ? "EXPIRED" : "Expiring Soon");

        Button checkBtn = new Button("Check Expiry");
        checkBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        checkBtn.setOnAction(e -> {
            expiryList.clear();
            for (Medicine m : medicines) {
                if (m.isExpired() || m.isExpiringSoon()) expiryList.add(m);
            }
            if (expiryList.isEmpty())
                showAlert("All Clear!", "No medicines are expired or expiring within 30 days.");
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        box.getChildren().addAll(title, info, checkBtn, table);
        return box;
    }

    private <T> void addColumn(TableView<T> table, String header, Function<T, String> valueExtractor) {
        TableColumn<T, String> col = new TableColumn<>(header);
        col.setCellValueFactory(data -> new SimpleStringProperty(valueExtractor.apply(data.getValue())));
        table.getColumns().add(col);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
