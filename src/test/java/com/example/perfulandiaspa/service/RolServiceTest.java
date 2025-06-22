package com.example.perfulandiaspa.service;

import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.repository.RolRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class RolServiceTest {

    private RolRepository rolRepository;
    private RolService rolService;

    @BeforeEach
    void setUp() {
        rolRepository = Mockito.mock(RolRepository.class);
        rolService = new RolService(rolRepository);
    }

    @Test
    public void testFindAll() {
    // Preparar datos
    Rol rol1 = new Rol();
    rol1.setId(1);
    rol1.setNombre("ADMIN");

    Rol rol2 = new Rol();
    rol2.setId(2);
    rol2.setNombre("CLIENTE");

    Rol rol3 = new Rol();
    rol3.setId(3);
    rol3.setNombre("VENDEDOR");

    // Configurar comportamiento del mock
    when(rolRepository.findAll()).thenReturn(Arrays.asList(rol1, rol2, rol3));

    // Ejecutar método a probar
    List<Rol> resultado = rolService.findAll();

    // Verificaciones
    assertNotNull(resultado);
    assertEquals(3, resultado.size());
    assertEquals("ADMIN", resultado.get(0).getNombre());
    assertEquals("CLIENTE", resultado.get(1).getNombre());
    assertEquals("VENDEDOR", resultado.get(2).getNombre());

    verify(rolRepository, times(1)).findAll();
    }
}
