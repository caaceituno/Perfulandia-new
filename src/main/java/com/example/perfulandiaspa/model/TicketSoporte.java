package com.example.perfulandiaspa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketSoporte {
    private int id;
    private int clienteId;
    private String asunto;
    private String mensaje;
    private String estado;
    private Date fechaCreacion;
}
