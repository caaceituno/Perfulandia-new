package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.Inventario;
import com.example.perfulandiaspa.model.Producto;
import com.example.perfulandiaspa.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
public class InventarioControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InventarioService inventarioService;
    @Autowired
    private ObjectMapper objectMapper;
    private Inventario inventario;
    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Perfume X");
        inventario = new Inventario();
        inventario.setId(1);
        inventario.setProducto(producto);
        inventario.setCantidad(20);
        inventario.setSucursal("Sucursal Central");
    }

    @Test
    public void testListarInventarios() throws Exception {
        when(inventarioService.findAll()).thenReturn(List.of(inventario));
        mockMvc.perform(get("/api/v1/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cantidad").value(20))
                .andExpect(jsonPath("$[0].sucursal").value("Sucursal Central"));
    }

    @Test
    public void testObtenerInventarioPorId() throws Exception {
        when(inventarioService.findById(1)).thenReturn(inventario);
        mockMvc.perform(get("/api/v1/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(20))
                .andExpect(jsonPath("$.sucursal").value("Sucursal Central"));
    }

    @Test
    public void testRegistroInventario() throws Exception {
        when(inventarioService.registroInventario(any(Inventario.class))).thenReturn(inventario);
        mockMvc.perform(post("/api/v1/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(20));
    }

    @Test
    public void testActualizarInventario() throws Exception {
        when(inventarioService.actualizarInventario(eq(1), any(Inventario.class))).thenReturn(inventario);
        mockMvc.perform(put("/api/v1/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(20));
    }

    @Test
    public void testEliminarInventario() throws Exception {
        doNothing().when(inventarioService).eliminarInventario(1);
        mockMvc.perform(delete("/api/v1/inventario/1"))
                .andExpect(status().isNoContent());
        verify(inventarioService, times(1)).eliminarInventario(1);
    }
}
