package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.Vendedor;
import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.service.VendedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendedorController.class)
public class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendedorService vendedorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Vendedor vendedor;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("vendedor1");
        usuario.setPassword("pass");
        usuario.setEmail("vendedor1@email.com");
        usuario.setEnabled(true);

        vendedor = new Vendedor();
        vendedor.setId(1);
        vendedor.setUsuario(usuario);
        vendedor.setNombreCompleto("Pedro Vendedor");
        vendedor.setSucursal("Sucursal Central");
        vendedor.setMetaMensual(1000000f);
    }

    @Test
    public void testListarVendedores() throws Exception {
        when(vendedorService.findAll()).thenReturn(List.of(vendedor));
        mockMvc.perform(get("/api/v1/vendedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Pedro Vendedor"))
                .andExpect(jsonPath("$[0].sucursal").value("Sucursal Central"))
                .andExpect(jsonPath("$[0].metaMensual").value(1000000f));
    }

    @Test
    public void testObtenerVendedorPorId() throws Exception {
        when(vendedorService.findById(1)).thenReturn(Optional.of(vendedor));
        mockMvc.perform(get("/api/v1/vendedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreCompleto").value("Pedro Vendedor"))
                .andExpect(jsonPath("$.sucursal").value("Sucursal Central"))
                .andExpect(jsonPath("$.metaMensual").value(1000000f));
    }

    @Test
    public void testRegistroCompleto() throws Exception {
        when(vendedorService.registrarUsuarioYVendedor(any(Vendedor.class))).thenReturn(vendedor);
        mockMvc.perform(post("/api/v1/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendedor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreCompleto").value("Pedro Vendedor"))
                .andExpect(jsonPath("$.sucursal").value("Sucursal Central"))
                .andExpect(jsonPath("$.metaMensual").value(1000000f));
    }

    @Test
    public void testActualizarVendedor() throws Exception {
        when(vendedorService.actualizarVendedor(eq(1), any(Vendedor.class))).thenReturn(vendedor);
        mockMvc.perform(put("/api/v1/vendedores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendedor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreCompleto").value("Pedro Vendedor"))
                .andExpect(jsonPath("$.sucursal").value("Sucursal Central"))
                .andExpect(jsonPath("$.metaMensual").value(1000000f));
    }

    @Test
    public void testEliminarVendedor() throws Exception {
        doNothing().when(vendedorService).eliminarVendedor(1);
        mockMvc.perform(delete("/api/v1/vendedores/1"))
                .andExpect(status().isNoContent());
        verify(vendedorService, times(1)).eliminarVendedor(1);
    }
}
