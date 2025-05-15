package com.example.PerfumeriaSPA.Model;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class Pruducto {
     private BigInteger id;
     private String nombre;
     private String descripcion;
     private float precio;
     private String categoria;
     private String marca;

    
}
