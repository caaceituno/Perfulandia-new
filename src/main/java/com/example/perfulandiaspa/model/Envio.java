package com.example.perfulandiaspa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Envio {
    private int id;
    private int ventaId;
    private String estado;
    private Date fechaEnvio;
    private String transportista;
}