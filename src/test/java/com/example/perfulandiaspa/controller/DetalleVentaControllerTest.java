package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.DetalleVenta;
import com.example.perfulandiaspa.service.DetalleVentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DetalleVentaController.class)
public class DetalleVentaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DetalleVentaService detalleVentaService;
    @Autowired
    private ObjectMapper objectMapper;
    private DetalleVenta detalleVenta;

    @BeforeEach
    void setUp() {
        detalleVenta = new DetalleVenta();
        detalleVenta.setId(1);
        detalleVenta.setProductoId(2);
        detalleVenta.setCantidad(3);
        detalleVenta.setPrecioUnitario(new BigInteger("5000"));
        //agrega más campos si tu modelo los tiene
    }

    @Test
    public void testListarDetalleVentas() throws Exception {
        when(detalleVentaService.findAll()).thenReturn(List.of(detalleVenta));
        mockMvc.perform(get("/api/v1/detalle-ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productoId").value(2))
                .andExpect(jsonPath("$[0].cantidad").value(3))
                .andExpect(jsonPath("$[0].precioUnitario").value(5000));
    }

    @Test
    public void testObtenerDetalleVentaPorId() throws Exception {
        when(detalleVentaService.findById(1)).thenReturn(Optional.of(detalleVenta));
        mockMvc.perform(get("/api/v1/detalle-ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productoId").value(2))
                .andExpect(jsonPath("$.cantidad").value(3))
                .andExpect(jsonPath("$.precioUnitario").value(5000));
    }

    @Test
    public void testRegistroDetalleVenta() throws Exception {
        when(detalleVentaService.registroDetalleVenta(any(DetalleVenta.class))).thenReturn(detalleVenta);
        mockMvc.perform(post("/api/v1/detalle-ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detalleVenta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productoId").value(2))
                .andExpect(jsonPath("$.cantidad").value(3))
                .andExpect(jsonPath("$.precioUnitario").value(5000));
    }

    @Test
    public void testActualizarDetalleVenta() throws Exception {
        when(detalleVentaService.actualizarDetalleVenta(eq(1), any(DetalleVenta.class))).thenReturn(detalleVenta);
        mockMvc.perform(put("/api/v1/detalle-ventas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detalleVenta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productoId").value(2))
                .andExpect(jsonPath("$.cantidad").value(3))
                .andExpect(jsonPath("$.precioUnitario").value(5000));
    }

    @Test
    public void testEliminarDetalleVenta() throws Exception {
        doNothing().when(detalleVentaService).eliminarDetalleVenta(1);
        mockMvc.perform(delete("/api/v1/detalle-ventas/1"))
                .andExpect(status().isNoContent());
        verify(detalleVentaService, times(1)).eliminarDetalleVenta(1);
    }
}
