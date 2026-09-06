//SE GENERA CLASE PARA AGREGAR LOS CARGOS ADICIONALES EN INVOICE Y PODER UTILIZARLOS.

package com.duoc.backend.Invoice;

import jakarta.persistence.Embeddable;

@Embeddable
public class AdditionalCharge {

    private String description;
    private Double cost;

    public AdditionalCharge() {
    }

    public AdditionalCharge(String description, Double cost) {
        this.description = description;
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}