package com.example.perfulandiaspa;

import com.example.perfulandiaspa.model.Venta;
import com.example.perfulandiaspa.model.DetalleVenta;
import com.example.perfulandiaspa.repository.VentaRepository;
import com.example.perfulandiaspa.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VentaServiceTest {
    private VentaRepository ventaRepository;
    private RestTemplate restTemplate;
    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        ventaRepository = mock(VentaRepository.class);
        restTemplate = mock(RestTemplate.class);
        ventaService = new VentaService(ventaRepository);
        //inyectar el mock de resttemplate usando reflexión
        try {
            var f1 = VentaService.class.getDeclaredField("restTemplate");
            f1.setAccessible(true);
            f1.set(ventaService, restTemplate);
            //inyectar urls dummy
            var f2 = VentaService.class.getDeclaredField("clienteServiceUrl");
            f2.setAccessible(true);
            f2.set(ventaService, "http://localhost:8080/api/v1/clientes");
            var f3 = VentaService.class.getDeclaredField("vendedorServiceUrl");
            f3.setAccessible(true);
            f3.set(ventaService, "http://localhost:8080/api/v1/vendedores");
            var f4 = VentaService.class.getDeclaredField("productosServiceUrl");
            f4.setAccessible(true);
            f4.set(ventaService, "http://localhost:8082/api/v1/productos");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindAll() {
        //probar findAll retorna lista
        Venta v1 = new Venta();
        v1.setId(1);
        Venta v2 = new Venta();
        v2.setId(2);
        when(ventaRepository.findAll()).thenReturn(Arrays.asList(v1, v2));
        List<Venta> resultado = ventaService.findAll();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(ventaRepository).findAll();
    }

    @Test
    void testFindById() {
        //probar findById existente
        Venta venta = new Venta();
        venta.setId(1);
        when(ventaRepository.findById(1)).thenReturn(Optional.of(venta));
        Optional<Venta> resultado = ventaService.findById(1);
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        verify(ventaRepository).findById(1);
    }

    @Test
    void testFindById_NoEncontrado() {
        //probar findById no existente
        when(ventaRepository.findById(99)).thenReturn(Optional.empty());
        Optional<Venta> resultado = ventaService.findById(99);
        assertFalse(resultado.isPresent());
        verify(ventaRepository).findById(99);
    }

    @Test
    void testRegistroVenta() {
        //probar registroVenta con mocks de servicios externos
        Venta venta = new Venta();
        venta.setClienteId(1);
        venta.setVendedorId(2);
        venta.setFecha(new Date());
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProductoId(5);
        detalle.setCantidad(2);
        venta.setDetalles(Arrays.asList(detalle));
        when(restTemplate.getForObject(contains("clientes"), eq(Object.class))).thenReturn(new Object());
        when(restTemplate.getForObject(contains("vendedores"), eq(Object.class))).thenReturn(new Object());
        when(restTemplate.getForObject(contains("productos"), eq(java.util.Map.class))).thenReturn(java.util.Map.of("precio", 1000));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> i.getArgument(0));
        Venta resultado = ventaService.registroVenta(venta);
        assertNotNull(resultado);
        assertEquals(1, resultado.getClienteId());
        assertEquals(2, resultado.getVendedorId());
        assertEquals(BigInteger.valueOf(2000), resultado.getTotal());
        verify(ventaRepository).save(venta);
    }

    @Test
    void testEliminarVenta() {
        //probar eliminarVenta
        doNothing().when(ventaRepository).deleteById(1);
        ventaService.eliminarVenta(1);
        verify(ventaRepository).deleteById(1);
    }

    @Test
    void testActualizarVenta_NoEncontrado() {
        //probar actualizarVenta con id inexistente
        when(ventaRepository.findById(99)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ventaService.actualizarVenta(99, new Venta());
        });
        assertTrue(exception.getMessage().contains("no encontrada"));
        verify(ventaRepository).findById(99);
        verify(ventaRepository, never()).save(any());
    }
}
