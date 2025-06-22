package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.Cliente;
import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.service.ClienteService;
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

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("cliente1");
        usuario.setPassword("pass");
        usuario.setEmail("cliente1@email.com");
        usuario.setEnabled(true);

        cliente = new Cliente();
        cliente.setId(1);
        cliente.setUsuario(usuario);
        cliente.setNombreCompleto("Juan Pérez");
        cliente.setRut("12345678-9");
        cliente.setTelefono("987654321");
        cliente.setDireccion("Calle Falsa 123");
    }

    @Test
    public void testListarClientes() throws Exception {
        when(clienteService.findAll()).thenReturn(List.of(cliente));
        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"));
    }

    @Test
    public void testObtenerClientePorId() throws Exception {
        when(clienteService.findById(1)).thenReturn(Optional.of(cliente));
        mockMvc.perform(get("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    public void testRegistroCompleto() throws Exception {
        when(clienteService.crearCliente(any(Cliente.class))).thenReturn(cliente);
        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    public void testActualizarCliente() throws Exception {
        when(clienteService.actualizarCliente(eq(1), any(Cliente.class))).thenReturn(cliente);
        mockMvc.perform(put("/api/v1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    public void testEliminarCliente() throws Exception {
        doNothing().when(clienteService).eliminarCliente(1);
        mockMvc.perform(delete("/api/v1/clientes/1"))
                .andExpect(status().isNoContent());
        verify(clienteService, times(1)).eliminarCliente(1);
    }
}
