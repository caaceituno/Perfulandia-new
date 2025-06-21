package com.example.perfulandiaspa;

import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.repository.UsuarioRepository;
import com.example.perfulandiaspa.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    public void testFindAll() {
        // Preparar datos
        Usuario u1 = new Usuario();
        u1.setId(1);
        u1.setUsername("admin");
        u1.setEmail("admin@example.com");
        
        Usuario u2 = new Usuario();
        u2.setId(2);
        u2.setUsername("cliente");
        u2.setEmail("cliente@example.com");

        // Configurar comportamiento del mock
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        // Ejecutar método a probar
        List<Usuario> resultado = usuarioService.findAll();

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("admin", resultado.get(0).getUsername());
        assertEquals("cliente", resultado.get(1).getUsername());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        // Preparar datos
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("admin");
        usuario.setEmail("admin@example.com");

        // Configurar comportamiento del mock
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecutar método y verificar - caso exitoso
        Optional<Usuario> resultado = usuarioService.findById(1);
        assertTrue(resultado.isPresent());
        assertEquals("admin", resultado.get().getUsername());
        
        // Ejecutar método y verificar - caso no encontrado
        Optional<Usuario> noEncontrado = usuarioService.findById(99);
        assertFalse(noEncontrado.isPresent());

        // Verificar que se llamó al repositorio correctamente
        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1)).findById(99);
    }

    @Test
    public void testCrearUsuario() {
        // Preparar datos
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevo");
        nuevoUsuario.setPassword("password");
        nuevoUsuario.setEmail("nuevo@example.com");
        nuevoUsuario.setEnabled(true);

        // Configurar comportamiento del mock
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(10); // Simular que se asignó ID al guardar
            return u;
        });

        // Ejecutar método
        Usuario guardado = usuarioService.crearUsuario(nuevoUsuario);

        // Verificaciones
        assertNotNull(guardado);
        assertEquals(10, guardado.getId());
        assertEquals("nuevo", guardado.getUsername());
        verify(usuarioRepository, times(1)).save(nuevoUsuario);
    }

    @Test
    public void testEliminarUsuario() {
        // Ejecutar método
        usuarioService.eliminarUsuario(5);
        
        // Verificar que se llamó al repositorio correctamente
        verify(usuarioRepository, times(1)).deleteById(5);
    }

    @Test
    public void testActualizarUsuario() {
        // Preparar datos
        Usuario existente = new Usuario();
        existente.setId(1);
        existente.setUsername("viejo");
        existente.setEmail("viejo@example.com");
        existente.setPassword("oldpass");
        existente.setEnabled(false);

        Usuario actualizado = new Usuario();
        actualizado.setUsername("nuevo");
        actualizado.setEmail("nuevo@example.com");
        actualizado.setPassword("newpass");
        actualizado.setEnabled(true);

        // Configurar comportamiento del mock
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar método
        Usuario resultado = usuarioService.actualizarUsuario(1, actualizado);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals("nuevo", resultado.getUsername());
        assertEquals("nuevo@example.com", resultado.getEmail());
        assertEquals("newpass", resultado.getPassword());
        assertTrue(resultado.isEnabled());
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(existente);
    }
    
    @Test
    public void testActualizarUsuario_NoEncontrado() {
        // Configurar comportamiento del mock
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecutar método y verificar que lanza excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario(99, new Usuario());
        });
        
        // Verificar mensaje de error
        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(usuarioRepository).findById(99);
        verify(usuarioRepository, never()).save(any());
    }
}