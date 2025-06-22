package com.example.perfulandiaspa.service;

import com.example.perfulandiaspa.model.Producto;
import com.example.perfulandiaspa.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {
    private ProductoRepository productoRepository;
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        productoService = new ProductoService(productoRepository);
    }

    @Test
    void testFindAll() {
        Producto p1 = new Producto();
        p1.setId(1);
        Producto p2 = new Producto();
        p2.setId(2);
        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
        List<Producto> resultado = productoService.findAll();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository).findAll();
    }

    @Test
    void testFindById() {
        Producto prod = new Producto();
        prod.setId(1);
        when(productoRepository.findById(1)).thenReturn(Optional.of(prod));
        Optional<Producto> resultado = productoService.findById(1);
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        verify(productoRepository).findById(1);
    }

    @Test
    void testFindById_NoEncontrado() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());
        Optional<Producto> resultado = productoService.findById(99);
        assertFalse(resultado.isPresent());
        verify(productoRepository).findById(99);
    }

    @Test
    void testGuardarProducto() {
        Producto prod = new Producto();
        when(productoRepository.save(any(Producto.class))).thenReturn(prod);
        Producto resultado = productoService.guardarProducto(prod);
        assertNotNull(resultado);
        verify(productoRepository).save(prod);
    }

    @Test
    void testEliminarProducto() {
        doNothing().when(productoRepository).deleteById(1);
        productoService.eliminarProducto(1);
        verify(productoRepository).deleteById(1);
    }

    @Test
    void testActualizarProducto() {
        Producto existente = new Producto();
        existente.setId(1);
        existente.setNombre("Antiguo");
        existente.setDescripcion("desc");
        existente.setPrecio(BigInteger.valueOf(1000));
        existente.setCategoria("cat");
        existente.setMarca("marca");

        Producto actualizado = new Producto();
        actualizado.setNombre("Nuevo");
        actualizado.setDescripcion("desc2");
        actualizado.setPrecio(BigInteger.valueOf(2000));
        actualizado.setCategoria("cat2");
        actualizado.setMarca("marca2");

        when(productoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        Producto resultado = productoService.actualizarProducto(1, actualizado);
        assertNotNull(resultado);
        assertEquals("Nuevo", resultado.getNombre());
        assertEquals("desc2", resultado.getDescripcion());
        assertEquals(BigInteger.valueOf(2000), resultado.getPrecio());
        assertEquals("cat2", resultado.getCategoria());
        assertEquals("marca2", resultado.getMarca());
        verify(productoRepository).findById(1);
        verify(productoRepository).save(existente);
    }

    @Test
    void testActualizarProducto_NoEncontrado() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.actualizarProducto(99, new Producto());
        });
        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(productoRepository).findById(99);
        verify(productoRepository, never()).save(any());
    }
}
