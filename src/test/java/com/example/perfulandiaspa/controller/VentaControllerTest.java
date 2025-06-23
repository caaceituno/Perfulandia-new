package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.Venta;
import com.example.perfulandiaspa.service.VentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
public class VentaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VentaService ventaService;
    @Autowired
    private ObjectMapper objectMapper;
    private Venta venta;

    @BeforeEach
    void setUp() {
        venta = new Venta();
        venta.setId(1);
        venta.setFecha(new Date());
        venta.setTotal(new java.math.BigInteger("10000"));

    }

    @Test
    public void testListarVentas() throws Exception {
        when(ventaService.findAll()).thenReturn(List.of(venta));
        mockMvc.perform(get("/api/v1/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].total").value(10000));
    }

    @Test
    public void testObtenerVentaPorId() throws Exception {
        when(ventaService.findById(1)).thenReturn(Optional.of(venta));
        mockMvc.perform(get("/api/v1/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.total").value(10000));
    }

    @Test
    public void testRegistroVenta() throws Exception {
        when(ventaService.registroVenta(any(Venta.class))).thenReturn(venta);
        mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.total").value(10000));
    }

    @Test
    public void testActualizarVenta() throws Exception {
        when(ventaService.actualizarVenta(eq(1), any(Venta.class))).thenReturn(venta);
        mockMvc.perform(put("/api/v1/ventas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.total").value(10000));
    }

    @Test
    public void testEliminarVenta() throws Exception {
        doNothing().when(ventaService).eliminarVenta(1);
        mockMvc.perform(delete("/api/v1/ventas/1"))
                .andExpect(status().isNoContent());
        verify(ventaService, times(1)).eliminarVenta(1);
    }
}
