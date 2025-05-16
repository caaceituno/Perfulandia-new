package com.example.perfulandiaspa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    private int id;
    private int usuarioId;
    private String nombreCompleto;
    private String rut;
    private String telefono;
    private String direccion;
}