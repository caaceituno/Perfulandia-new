package com.example.perfulandiaspa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venta {
    private int id;
    private int clienteId;
    private int vendedorId;
    private Date fecha;
    private float total;
}
