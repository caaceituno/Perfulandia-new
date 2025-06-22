package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.service.RolService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
public class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    private Rol rolAdmin;
    private Rol rolCliente;
    private Rol rolVendedor;

    @BeforeEach
    void setUp() {
        rolAdmin = new Rol();
        rolAdmin.setId(1);
        rolAdmin.setNombre("admin");
        rolCliente = new Rol();
        rolCliente.setId(2);
        rolCliente.setNombre("cliente");
        rolVendedor = new Rol();
        rolVendedor.setId(3);
        rolVendedor.setNombre("vendedor");
    }

    @Test
    public void testListarRoles() throws Exception {
        when(rolService.findAll()).thenReturn(List.of(rolAdmin, rolCliente, rolVendedor));
        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("admin"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("cliente"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].nombre").value("vendedor"));
    }
}
