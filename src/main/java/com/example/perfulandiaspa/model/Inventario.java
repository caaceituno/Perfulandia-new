package com.example.perfulandiaspa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {
    private int id;
    private int productoId;
    private Integer cantidad;
    private String sucursal;
}