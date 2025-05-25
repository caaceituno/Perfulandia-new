package com.example.perfulandiaspa.service;

import java.util.List;

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

    //metodo para guardar un producto
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    //eliminar producto
    public void eliminarProducto(int id) {
        productoRepository.deleteById(id);
    }

    //actualizar producto
    public Producto actualizarProducto(Producto producto) {
        return productoRepository.save(producto);
    }
}
