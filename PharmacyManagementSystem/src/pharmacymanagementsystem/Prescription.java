import java.time.LocalDate;

public class Prescription implements Manageable {

    private String prescriptionId;
    private String patientName;
    private String doctorName;
    private String medicines;
    private LocalDate date;
    private String prescriptionType;

    public Prescription(String prescriptionId, String patientName, String doctorName,
                        String medicines, LocalDate date, String prescriptionType) {
        this.prescriptionId = prescriptionId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.medicines = medicines;
        this.date = date;
        this.prescriptionType = prescriptionType;
    }

  
    public String getSummary() {
        return "Prescription ID: " + prescriptionId
                + " | Patient: " + patientName
                + " | Doctor: " + doctorName
                + " | Type: " + prescriptionType
                + " | Date: " + date
                + " | Medicines: " + medicines;
    }

  
    public boolean isValid() {
        return prescriptionId != null && !prescriptionId.isEmpty()
                && patientName != null && !patientName.isEmpty()
                && date != null;
    }

    public String getPrescriptionId() { return prescriptionId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getMedicines() { return medicines; }
    public LocalDate getDate() { return date; }
    public String getPrescriptionType() { return prescriptionType; }
}
