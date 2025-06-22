package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("usuario1");
        usuario.setPassword("pass");
        usuario.setEmail("usuario1@email.com");
        usuario.setEnabled(true);

    }

    @Test
    public void testListarUsuarios() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuario));
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("usuario1"))
                .andExpect(jsonPath("$[0].email").value("usuario1@email.com"));
    }

    @Test
    public void testObtenerUsuarioPorId() throws Exception {
        when(usuarioService.findById(1)).thenReturn(Optional.of(usuario));
        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("usuario1"))
                .andExpect(jsonPath("$.email").value("usuario1@email.com"));
    }

    @Test
    public void testRegistroCompleto() throws Exception {
        when(usuarioService.crearUsuario(any(Usuario.class))).thenReturn(usuario);
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("usuario1"))
                .andExpect(jsonPath("$.email").value("usuario1@email.com"));
    }

    @Test
    public void testActualizarUsuario() throws Exception {
        when(usuarioService.actualizarUsuario(eq(1), any(Usuario.class))).thenReturn(usuario);
        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("usuario1"))
                .andExpect(jsonPath("$.email").value("usuario1@email.com"));
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(1);
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());
        verify(usuarioService, times(1)).eliminarUsuario(1);
    }
}
