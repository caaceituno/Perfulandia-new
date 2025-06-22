package com.example.perfulandiaspa.service;

import com.example.perfulandiaspa.model.Cliente;
import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.repository.ClienteRepository;
import com.example.perfulandiaspa.repository.RolRepository;
import com.example.perfulandiaspa.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public void testCrearCliente() {
    // Datos de entrada simulados
    Rol rol = new Rol();
    rol.setId(1);
    rol.setNombre("CLIENTE");

    Usuario usuario = new Usuario();
    usuario.setId(10);
    usuario.setUsername("usuario_cliente");
    usuario.setPassword("123456");
    usuario.setRol(rol);
    usuario.setEnabled(true);

    Cliente cliente = new Cliente();
    cliente.setId(100);
    cliente.setNombreCompleto("Cliente Prueba");
    cliente.setUsuario(usuario);

    // Configurar mocks
    when(rolRepository.findByNombre("CLIENTE")).thenReturn(rol);
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
    when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

    // Ejecutar
    Cliente resultado = clienteService.crearCliente(cliente);

    // Verificaciones
    assertNotNull(resultado);
    assertEquals(100, resultado.getId());
    assertEquals("Cliente Prueba", resultado.getNombreCompleto());
    assertEquals("CLIENTE", resultado.getUsuario().getRol().getNombre());
    assertTrue(resultado.getUsuario().isEnabled());

    // Verificar interacciones con los repositorios
    verify(rolRepository).findByNombre("CLIENTE");
    verify(usuarioRepository).save(any(Usuario.class));
    verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void testActualizarCliente() {
        Cliente existente = new Cliente();
        existente.setId(1);
        existente.setNombreCompleto("Nombre Antiguo");
        existente.setRut("11111111-1");
        existente.setTelefono("123456789");
        existente.setDireccion("Calle Vieja 1");

        Cliente actualizado = new Cliente();
        actualizado.setNombreCompleto("Nombre Nuevo");
        actualizado.setRut("22222222-2");
        actualizado.setTelefono("987654321");
        actualizado.setDireccion("Calle Nueva 2");

        when(clienteRepository.findById(1)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente resultado = clienteService.actualizarCliente(1, actualizado);

        assertNotNull(resultado);
        assertEquals("Nombre Nuevo", resultado.getNombreCompleto());
        assertEquals("22222222-2", resultado.getRut());
        assertEquals("987654321", resultado.getTelefono());
        assertEquals("Calle Nueva 2", resultado.getDireccion());
        verify(clienteRepository).findById(1);
        verify(clienteRepository).save(existente);
    }

    @Test
    void testActualizarCliente_NoEncontrado() {
    when(clienteRepository.findById(99)).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        clienteService.actualizarCliente(99, new Cliente());
    });

    assertTrue(exception.getMessage().contains("no encontrado"));
    verify(clienteRepository).findById(99);
    verify(clienteRepository, never()).save(any());
    }

    @Test
    void testEliminarCliente() {
    Cliente cliente = new Cliente();
    cliente.setId(1);

    Usuario usuario = new Usuario();
    usuario.setId(10);

    cliente.setUsuario(usuario);

    when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
    doNothing().when(clienteRepository).deleteById(1);
    doNothing().when(usuarioRepository).deleteById(10);

    clienteService.eliminarCliente(1);

    verify(clienteRepository).findById(1);
    verify(clienteRepository).deleteById(1);
    verify(usuarioRepository).deleteById(10);
    }

    @Test
    void testEliminarCliente_NoEncontrado() {
    // Simular que el cliente no existe
    when(clienteRepository.findById(99)).thenReturn(Optional.empty());

    // Verificar que se lanza una excepción al intentar eliminar
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        clienteService.eliminarCliente(99);
    });

    // Afirmaciones sobre la excepción lanzada
    assertTrue(exception.getMessage().contains("no encontrado"));

    // Verificar que no se realizaron eliminaciones
    verify(clienteRepository).findById(99);
    verify(clienteRepository, never()).deleteById(anyInt());
    verify(usuarioRepository, never()).deleteById(anyInt());
    }

    @Test
    void testFindAll() {
    Cliente c1 = new Cliente();
    c1.setId(1);

    Cliente c2 = new Cliente();
    c2.setId(2);

    when(clienteRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

    List<Cliente> resultado = clienteService.findAll();

    assertNotNull(resultado);
    assertEquals(2, resultado.size());
    verify(clienteRepository).findAll();
    }

    @Test
    void testFindById() {
    Cliente cliente = new Cliente();
    cliente.setId(1);

    when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

    Optional<Cliente> resultado = clienteService.findById(1);

    assertTrue(resultado.isPresent());
    assertEquals(1, resultado.get().getId());
    verify(clienteRepository).findById(1);
    }

    @Test
    void testFindById_NoEncontrado() {
    when(clienteRepository.findById(99)).thenReturn(Optional.empty());

    Optional<Cliente> resultado = clienteService.findById(99);

    assertFalse(resultado.isPresent());
        verify(clienteRepository).findById(99);
    }

}
