package com.example.perfulandiaspa.model;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ventas")
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cliente_id", nullable = false)
    private int clienteId;

    @Column(name = "vendedor_id", nullable = false)
    private int vendedorId;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column(nullable = false)
    private BigInteger total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetalleVenta> detalles;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> clienteDetalles;
    
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> vendedorDetalles;

    //metodo para calcular el total de la venta
    public void calcularTotal() {
    int total = 0;
    if (detalles != null) {
        for (DetalleVenta detalle : detalles) {
            int precio = detalle.getPrecioUnitario().intValue();
            int cantidad = detalle.getCantidad();
            total += precio * cantidad;
        }
    }
    this.total = new BigInteger(String.valueOf(total));
    }
}
