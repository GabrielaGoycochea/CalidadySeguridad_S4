package com.duoc.backend.Invoice;
 
import com.duoc.backend.Care.Care;
import com.duoc.backend.Care.CareRepository;
import com.duoc.backend.Medication.Medication;
import com.duoc.backend.Medication.MedicationRepository;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
 
@Service
public class InvoiceService {
 
    @Autowired
    private InvoiceRepository invoiceRepository;
 
    @Autowired
    private MedicationRepository medicationRepository;
 
    @Autowired
    private CareRepository careRepository;
 
    public Iterable<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
 
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }
 
    public Invoice saveInvoice(Invoice invoice) {
        // Validar que los medicamentos existen
        List<Medication> validMedications = StreamSupport.stream(
                medicationRepository.findAllById(
                        invoice.getMedications().stream().map(Medication::getId).collect(Collectors.toList())
                ).spliterator(), false
        ).collect(Collectors.toList());
        if (validMedications.size() != invoice.getMedications().size()) {
            throw new IllegalArgumentException("Algunos medicamentos no existen en la base de datos.");
        }
 
        // Validar que los servicios existen
        List<Care> validCares = StreamSupport.stream(
                careRepository.findAllById(
                invoice.getCares().stream().map(Care::getId).collect(Collectors.toList())
                ).spliterator(), 
            false).collect(Collectors.toList());
        if (validCares.size() != invoice.getCares().size()) {
            throw new IllegalArgumentException("Algunos servicios no existen en la base de datos.");
        }
 
        // NUEVO: cargos adicionales (pueden venir vacíos o nulos)
        List<AdditionalCharge> additionalCharges = invoice.getAdditionalCharges();
        if (additionalCharges == null) {
            additionalCharges = Collections.emptyList();
        }
 
        // Calcular el costo total: servicios + medicamentos + cargos adicionales
        double totalCareCost = validCares.stream()
                .mapToDouble(Care::getCost)
                .sum();
 
        double totalMedicationCost = validMedications.stream()
                .mapToDouble(Medication::getCost)
                .sum();
 
        double totalAdditionalCost = additionalCharges.stream()
                .mapToDouble(AdditionalCharge::getCost)
                .sum();
 
        invoice.setTotalCost(totalCareCost + totalMedicationCost + totalAdditionalCost);
 
        // Guardar la factura
        return invoiceRepository.save(invoice);
    }
 
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}
 
