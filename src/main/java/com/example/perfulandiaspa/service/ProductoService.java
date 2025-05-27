package com.example.perfulandiaspa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.perfulandiaspa.model.Producto;
import com.example.perfulandiaspa.repository.ProductoRepository; 

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    //recibiendo instancia de ProductoRepository como parámetro para asignar al atributo
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    //metodo para obtener los productos
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    //metodo para obtener una venta por ID
    public Optional<Producto> findById(int id) {
        return productoRepository.findById(id);
    }

    //metodo para guardar un producto
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    //eliminar producto
    public void eliminarProducto(int id) {
        productoRepository.deleteById(id);
    }

    //actualizar producto (solo campos enviados)
    public Producto actualizarProducto(int id, Producto producto) {
        Producto productoExistente = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto con ID " + id + " no encontrado"));

        //actualizar solo los campos que no son nulos
        if (producto.getNombre() != null) {
            productoExistente.setNombre(producto.getNombre());
        }
        if (producto.getDescripcion() != null) {
            productoExistente.setDescripcion(producto.getDescripcion());
        }
        if (producto.getPrecio() != null) {
            productoExistente.setPrecio(producto.getPrecio());
        }
        if (producto.getCategoria() != null) {
            productoExistente.setCategoria(producto.getCategoria());
        }
        if (producto.getMarca() != null) {
            productoExistente.setMarca(producto.getMarca());
        }
        return productoRepository.save(productoExistente);
    }
}
