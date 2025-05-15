package com.example.PerfumeriaSPA.Model;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Cliente {
    private BigInteger id;
    private BigInteger usuarioId;
    private  String nombreCompleto;
    private String rut;
    private String telefono;
    private String direccion;

    
}
