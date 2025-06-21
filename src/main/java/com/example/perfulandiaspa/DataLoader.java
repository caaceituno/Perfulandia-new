package com.example.perfulandiaspa;

import com.example.perfulandiaspa.model.*;
import com.example.perfulandiaspa.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    private Faker faker = new Faker();

    @Override
    public void run(String... args) throws Exception {
        // Crear productos fake
        for (int i = 0; i < 10; i++) {
            Producto producto = new Producto();
            producto.setNombre(faker.commerce().productName());
            producto.setDescripcion(faker.lorem().sentence());
            producto.setPrecio(BigInteger.valueOf(faker.number().numberBetween(1000, 10000)));
            producto.setCategoria(faker.commerce().department());
            producto = productoRepository.save(producto);

            // Crear inventario para el producto
            Inventario inventario = new Inventario();
            inventario.setProducto(producto);
            inventario.setCantidad(faker.number().numberBetween(10, 100));
            inventario.setSucursal(faker.address().cityName());
            inventarioRepository.save(inventario);
        }
    }
}
