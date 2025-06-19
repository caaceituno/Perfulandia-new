package com.example.perfulandiaspa;

import com.example.perfulandiaspa.service.ClienteService;

import com.example.perfulandiaspa.model.Cliente;
import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.repository.ClienteRepository;
import com.example.perfulandiaspa.repository.RolRepository;
import com.example.perfulandiaspa.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    ClienteRepository clienteRepository;
    UsuarioRepository usuarioRepository;
    RolRepository rolRepository;
    ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteRepository = mock(ClienteRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        rolRepository = mock(RolRepository.class);

        clienteService = new ClienteService(clienteRepository);
        // inyectar también los otros mocks (como no tienes constructor completo, usamos reflexión)
        // o mejor, hacemos un setter o public fields (acá para simplicidad usamos reflection):
        try {
            var f1 = ClienteService.class.getDeclaredField("usuarioRepository");
            f1.setAccessible(true);
            f1.set(clienteService, usuarioRepository);

            var f2 = ClienteService.class.getDeclaredField("rolRepository");
            f2.setAccessible(true);
            f2.set(clienteService, rolRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCrearCliente() {
        // Preparar datos
        Rol rolCliente = new Rol();
        rolCliente.setId(1);
        rolCliente.setNombre("CLIENTE");

        Usuario usuario = new Usuario();
        usuario.setUsername("usuario1");
        usuario.setPassword("pass");
        usuario.setEmail("usuario1@email.com");

        Cliente cliente = new Cliente();
        cliente.setNombreCompleto("Cliente Prueba");
        cliente.setRut("12345678-9");
        cliente.setTelefono("987654321");
        cliente.setDireccion("Calle Falsa 123");
        cliente.setUsuario(usuario);

        // Mock behaviors
        when(rolRepository.findByNombre("CLIENTE")).thenReturn(rolCliente);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(10); // simular que se le asignó ID al guardar
            return u;
        });
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> {
            Cliente c = i.getArgument(0);
            c.setId(100);
            return c;
        });

        // Ejecutar método
        Cliente resultado = clienteService.crearCliente(cliente);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(100, resultado.getId());
        assertEquals("Cliente Prueba", resultado.getNombreCompleto());

        // Verificar que el rol se asignó y usuario quedó habilitado
        assertEquals("CLIENTE", resultado.getUsuario().getRol().getNombre());
        assertTrue(resultado.getUsuario().isEnabled());

        // Verificar que se llamó a los repositorios de guardado
        verify(rolRepository).findByNombre("CLIENTE");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(clienteRepository).save(any(Cliente.class));
    }
}
