package com.example.perfulandiaspa.service;

import com.example.perfulandiaspa.model.DetalleVenta;
import com.example.perfulandiaspa.model.Venta;
import com.example.perfulandiaspa.repository.DetalleVentaRepository;
import com.example.perfulandiaspa.repository.VentaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DetalleVentaServiceTest {
    private DetalleVentaRepository detalleVentaRepository;
    private VentaRepository ventaRepository;
    private RestTemplate restTemplate;
    private DetalleVentaService detalleVentaService;

    @BeforeEach
    void setUp() {
        detalleVentaRepository = mock(DetalleVentaRepository.class);
        ventaRepository = mock(VentaRepository.class);
        restTemplate = mock(RestTemplate.class);
        detalleVentaService = new DetalleVentaService(detalleVentaRepository);
        //inyectar mocks usando reflexión
        try {
            var f1 = DetalleVentaService.class.getDeclaredField("restTemplate");
            f1.setAccessible(true);
            f1.set(detalleVentaService, restTemplate);
            var f2 = DetalleVentaService.class.getDeclaredField("ventaRepository");
            f2.setAccessible(true);
            f2.set(detalleVentaService, ventaRepository);
            var f3 = DetalleVentaService.class.getDeclaredField("productoServiceUrl");
            f3.setAccessible(true);
            f3.set(detalleVentaService, "http://localhost:8082/api/v1/productos");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindAll() {
        //probar findAll retorna lista
        DetalleVenta d1 = new DetalleVenta();
        d1.setId(1);
        DetalleVenta d2 = new DetalleVenta();
        d2.setId(2);
        when(detalleVentaRepository.findAll()).thenReturn(Arrays.asList(d1, d2));
        var resultado = detalleVentaService.findAll();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(detalleVentaRepository).findAll();
    }

    @Test
    void testFindById() {
        //probar findById existente
        DetalleVenta detalle = new DetalleVenta();
        detalle.setId(1);
        when(detalleVentaRepository.findById(1)).thenReturn(Optional.of(detalle));
        Optional<DetalleVenta> resultado = detalleVentaService.findById(1);
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        verify(detalleVentaRepository).findById(1);
    }

    @Test
    void testFindById_NoEncontrado() {
        //probar findById no existente
        when(detalleVentaRepository.findById(99)).thenReturn(Optional.empty());
        Optional<DetalleVenta> resultado = detalleVentaService.findById(99);
        assertFalse(resultado.isPresent());
        verify(detalleVentaRepository).findById(99);
    }

    @Test
    void testRegistroDetalleVenta() {
        //probar registroDetalleVenta con producto válido
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProductoId(5);
        detalle.setCantidad(3);
        when(restTemplate.getForObject(contains("productos"), eq(java.util.Map.class))).thenReturn(java.util.Map.of("precio", 1000));
        when(detalleVentaRepository.save(any(DetalleVenta.class))).thenAnswer(i -> i.getArgument(0));
        DetalleVenta resultado = detalleVentaService.registroDetalleVenta(detalle);
        assertNotNull(resultado);
        assertEquals(BigInteger.valueOf(1000), resultado.getPrecioUnitario());
        verify(detalleVentaRepository).save(detalle);
    }

    @Test
    void testEliminarDetalleVenta() {
        //probar eliminarDetalleVenta recalcula total
        DetalleVenta detalle = new DetalleVenta();
        detalle.setId(1);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(BigInteger.valueOf(1000));
        Venta venta = new Venta();
        venta.setId(10);
        DetalleVenta otro = new DetalleVenta();
        otro.setId(2);
        otro.setCantidad(1);
        otro.setPrecioUnitario(BigInteger.valueOf(500));
        venta.setDetalles(Arrays.asList(detalle, otro));
        detalle.setVenta(venta);
        when(detalleVentaRepository.findById(1)).thenReturn(Optional.of(detalle));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(detalleVentaRepository).deleteById(1);
        detalleVentaService.eliminarDetalleVenta(1);
        verify(detalleVentaRepository).deleteById(1);
        verify(ventaRepository).save(venta);
        assertEquals(BigInteger.valueOf(500), venta.getTotal());
    }

    @Test
    void testActualizarDetalleVenta_NoEncontrado() {
        //probar actualizarDetalleVenta con id inexistente
        when(detalleVentaRepository.findById(99)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            detalleVentaService.actualizarDetalleVenta(99, new DetalleVenta());
        });
        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(detalleVentaRepository).findById(99);
        verify(detalleVentaRepository, never()).save(any());
    }
}
