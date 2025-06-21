package com.example.perfulandiaspa;

import com.example.perfulandiaspa.model.Vendedor;
import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.repository.VendedorRepository;
import com.example.perfulandiaspa.repository.RolRepository;
import com.example.perfulandiaspa.repository.UsuarioRepository;
import com.example.perfulandiaspa.service.VendedorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VendedorServiceTest {

    private VendedorRepository vendedorRepository;
    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private VendedorService vendedorService;

    @BeforeEach
    void setUp() {
        vendedorRepository = mock(VendedorRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        rolRepository = mock(RolRepository.class);

        vendedorService = new VendedorService(vendedorRepository);
        
        // Inyectar los otros repositorios usando reflexión
        try {
            var f1 = VendedorService.class.getDeclaredField("usuarioRepository");
            f1.setAccessible(true);
            f1.set(vendedorService, usuarioRepository);

            var f2 = VendedorService.class.getDeclaredField("rolRepository");
            f2.setAccessible(true);
            f2.set(vendedorService, rolRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindAll() {
        // Preparar datos
        Vendedor v1 = new Vendedor();
        v1.setId(1);
        v1.setNombreCompleto("Vendedor 1");
        v1.setSucursal("Sucursal Central");
        
        Vendedor v2 = new Vendedor();
        v2.setId(2);
        v2.setNombreCompleto("Vendedor 2");
        v2.setSucursal("Sucursal Norte");

        // Configurar comportamiento del mock
        when(vendedorRepository.findAll()).thenReturn(Arrays.asList(v1, v2));

        // Ejecutar método a probar
        List<Vendedor> resultado = vendedorService.findAll();

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Vendedor 1", resultado.get(0).getNombreCompleto());
        assertEquals("Vendedor 2", resultado.get(1).getNombreCompleto());
        verify(vendedorRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        // Preparar datos
        Vendedor vendedor = new Vendedor();
        vendedor.setId(1);
        vendedor.setNombreCompleto("Vendedor Test");
        vendedor.setSucursal("Sucursal Test");
        vendedor.setMetaMensual(5000f);

        // Configurar comportamiento del mock
        when(vendedorRepository.findById(1)).thenReturn(Optional.of(vendedor));
        when(vendedorRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecutar método y verificar - caso exitoso
        Optional<Vendedor> resultado = vendedorService.findById(1);
        assertTrue(resultado.isPresent());
        assertEquals("Vendedor Test", resultado.get().getNombreCompleto());
        
        // Ejecutar método y verificar - caso no encontrado
        Optional<Vendedor> noEncontrado = vendedorService.findById(99);
        assertFalse(noEncontrado.isPresent());

        // Verificar que se llamó al repositorio correctamente
        verify(vendedorRepository, times(1)).findById(1);
        verify(vendedorRepository, times(1)).findById(99);
    }

    @Test
    public void testRegistroVendedor() {
        // Preparar datos
        Vendedor nuevoVendedor = new Vendedor();
        nuevoVendedor.setNombreCompleto("Nuevo Vendedor");
        nuevoVendedor.setSucursal("Sucursal Sur");
        nuevoVendedor.setMetaMensual(6000f);

        // Configurar comportamiento del mock
        when(vendedorRepository.save(any(Vendedor.class))).thenAnswer(invocation -> {
            Vendedor v = invocation.getArgument(0);
            v.setId(10);
            return v;
        });

        // Ejecutar método
        Vendedor guardado = vendedorService.registroVendedor(nuevoVendedor);

        // Verificaciones
        assertNotNull(guardado);
        assertEquals(10, guardado.getId());
        assertEquals("Nuevo Vendedor", guardado.getNombreCompleto());
        verify(vendedorRepository, times(1)).save(nuevoVendedor);
    }

    @Test
    public void testRegistrarUsuarioYVendedor() {
        // Preparar datos
        Rol rolVendedor = new Rol();
        rolVendedor.setId(3);
        rolVendedor.setNombre("VENDEDOR");

        Usuario usuario = new Usuario();
        usuario.setUsername("vendedor1");
        usuario.setPassword("pass123");
        usuario.setEmail("vendedor1@example.com");

        Vendedor vendedor = new Vendedor();
        vendedor.setNombreCompleto("Juan Pérez");
        vendedor.setSucursal("Sucursal Este");
        vendedor.setMetaMensual(5500f);
        vendedor.setUsuario(usuario);

        // Configurar comportamiento del mock
        when(rolRepository.findByNombre("VENDEDOR")).thenReturn(rolVendedor);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(20);
            return u;
        });
        when(vendedorRepository.save(any(Vendedor.class))).thenAnswer(i -> {
            Vendedor v = i.getArgument(0);
            v.setId(30);
            return v;
        });

        // Ejecutar método
        Vendedor resultado = vendedorService.registrarUsuarioYVendedor(vendedor);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(30, resultado.getId());
        assertEquals("Juan Pérez", resultado.getNombreCompleto());
        
        // Verificar que el rol se asignó y usuario quedó habilitado
        assertEquals("VENDEDOR", resultado.getUsuario().getRol().getNombre());
        assertEquals(20, resultado.getUsuario().getId());
        assertTrue(resultado.getUsuario().isEnabled());

        // Verificar que se llamó a los repositorios correctamente
        verify(rolRepository).findByNombre("VENDEDOR");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(vendedorRepository).save(any(Vendedor.class));
    }

    @Test
    public void testEliminarVendedor() {
        // Ejecutar método
        vendedorService.eliminarVendedor(5);
        
        // Verificar que se llamó al repositorio correctamente
        verify(usuarioRepository, times(1)).deleteById(5);
    }

    @Test
    public void testActualizarVendedor() {
        // Preparar datos
        Vendedor existente = new Vendedor();
        existente.setId(1);
        existente.setNombreCompleto("Nombre Antiguo");
        existente.setSucursal("Sucursal Antigua");
        existente.setMetaMensual(3000f);

        Vendedor actualizado = new Vendedor();
        actualizado.setNombreCompleto("Nombre Nuevo");
        actualizado.setSucursal("Sucursal Nueva");
        actualizado.setMetaMensual(7000f);

        // Configurar comportamiento del mock
        when(vendedorRepository.findById(1)).thenReturn(Optional.of(existente));
        when(vendedorRepository.save(any(Vendedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar método
        Vendedor resultado = vendedorService.actualizarVendedor(1, actualizado);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals("Nombre Nuevo", resultado.getNombreCompleto());
        assertEquals("Sucursal Nueva", resultado.getSucursal());
        assertEquals(7000f, resultado.getMetaMensual());
        verify(vendedorRepository).findById(1);
        verify(vendedorRepository).save(existente);
    }
    
    @Test
    public void testActualizarVendedor_NoEncontrado() {
        // Configurar comportamiento del mock
        when(vendedorRepository.findById(99)).thenReturn(Optional.empty());

        // Ejecutar método y verificar que lanza excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vendedorService.actualizarVendedor(99, new Vendedor());
        });
        
        // Verificar mensaje de error
        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(vendedorRepository).findById(99);
        verify(vendedorRepository, never()).save(any());
    }
}