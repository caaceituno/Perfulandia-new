package com.example.PerfumeriaSPA.Model;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Inventario {
    private BigInteger id;
    private BigInteger productoId;
    private int cantidad;
    private String ubicacion;
    
    
    
}
