package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.Producto;
import com.example.perfulandiaspa.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductoService productoService;
    @Autowired
    private ObjectMapper objectMapper;
    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Perfume X");
        producto.setDescripcion("Aroma floral");
        producto.setPrecio(BigInteger.valueOf(5000));
        producto.setCategoria("Fragancias");
        producto.setMarca("MarcaTest");
    }

    @Test
    public void testListarProductos() throws Exception {
        when(productoService.findAll()).thenReturn(List.of(producto));
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Perfume X"));
    }

    @Test
    public void testObtenerProductoPorId() throws Exception {
        when(productoService.findById(1)).thenReturn(Optional.of(producto));
        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").value(1))
                .andExpect(jsonPath(".nombre").value("Perfume X"));
    }

    @Test
    public void testRegistroProducto() throws Exception {
        when(productoService.guardarProducto(any(Producto.class))).thenReturn(producto);
        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(".id").value(1))
                .andExpect(jsonPath(".nombre").value("Perfume X"));
    }

    @Test
    public void testActualizarProducto() throws Exception {
        when(productoService.actualizarProducto(eq(1), any(Producto.class))).thenReturn(producto);
        mockMvc.perform(put("/api/v1/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").value(1))
                .andExpect(jsonPath(".nombre").value("Perfume X"));
    }

    @Test
    public void testEliminarProducto() throws Exception {
        doNothing().when(productoService).eliminarProducto(1);
        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNoContent());
        verify(productoService, times(1)).eliminarProducto(1);
    }
}
