package com.example.perfulandiaspa.service;

import com.example.perfulandiaspa.model.Inventario;
import com.example.perfulandiaspa.model.Producto;
import com.example.perfulandiaspa.repository.InventarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventarioServiceTest {
    private InventarioRepository inventarioRepository;
    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        inventarioRepository = mock(InventarioRepository.class);
        inventarioService = new InventarioService(inventarioRepository);
    }

    @Test
    void testFindAll() {
        Inventario i1 = new Inventario();
        i1.setId(1);
        Inventario i2 = new Inventario();
        i2.setId(2);
        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(i1, i2));
        List<Inventario> resultado = inventarioService.findAll();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(inventarioRepository).findAll();
    }

    @Test
    void testFindById() {
        Inventario inv = new Inventario();
        inv.setId(1);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inv));
        Inventario resultado = inventarioService.findById(1);
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(inventarioRepository).findById(1);
    }

    @Test
    void testFindById_NoEncontrado() {
        when(inventarioRepository.findById(99)).thenReturn(Optional.empty());
        Inventario resultado = inventarioService.findById(99);
        assertNull(resultado);
        verify(inventarioRepository).findById(99);
    }

    @Test
    void testRegistroInventario() {
        Inventario inv = new Inventario();
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inv);
        Inventario resultado = inventarioService.registroInventario(inv);
        assertNotNull(resultado);
        verify(inventarioRepository).save(inv);
    }

    @Test
    void testEliminarInventario() {
        doNothing().when(inventarioRepository).deleteById(1);
        inventarioService.eliminarInventario(1);
        verify(inventarioRepository).deleteById(1);
    }

    @Test
    void testActualizarInventario() {
        Inventario existente = new Inventario();
        existente.setId(1);
        existente.setCantidad(10);
        existente.setSucursal("Sucursal A");
        Producto prod = new Producto();
        existente.setProducto(prod);

        Inventario actualizado = new Inventario();
        actualizado.setCantidad(20);
        actualizado.setSucursal("Sucursal B");
        actualizado.setProducto(prod);

        when(inventarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(i -> i.getArgument(0));

        Inventario resultado = inventarioService.actualizarInventario(1, actualizado);
        assertNotNull(resultado);
        assertEquals(20, resultado.getCantidad());
        assertEquals("Sucursal B", resultado.getSucursal());
        verify(inventarioRepository).findById(1);
        verify(inventarioRepository).save(existente);
    }

    @Test
    void testActualizarInventario_NoEncontrado() {
        when(inventarioRepository.findById(99)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.actualizarInventario(99, new Inventario());
        });
        assertTrue(exception.getMessage().contains("no encontrado"));
        verify(inventarioRepository).findById(99);
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void testActualizarInventario_CantidadNegativa() {
        Inventario existente = new Inventario();
        existente.setId(1);
        existente.setCantidad(10);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(existente));
        Inventario actualizado = new Inventario();
        actualizado.setCantidad(-5);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.actualizarInventario(1, actualizado);
        });
        assertTrue(exception.getMessage().contains("negativa"));
        verify(inventarioRepository).findById(1);
        verify(inventarioRepository, never()).save(any());
    }
}
