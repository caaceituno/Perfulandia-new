package com.example.perfulandiaspa;

import com.example.perfulandiaspa.model.DetalleVenta;
import com.example.perfulandiaspa.model.Venta;
import com.example.perfulandiaspa.repository.VentaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private VentaRepository ventaRepository;

    private Faker faker = new Faker();

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 5; i++) {
            //crear una venta fake con cliente y vendedor aleatorio
            Venta venta = new Venta();
            venta.setClienteId(faker.number().numberBetween(1, 10)); //ID de cliente simulado
            venta.setVendedorId(faker.number().numberBetween(1, 5)); //ID de vendedor simulado
            venta.setFecha(new Date()); //fecha actual
            venta.setTotal(BigInteger.ZERO); //el total se calculará sumando los detalles

            //lista para los detalles de la venta
            List<DetalleVenta> detalles = new ArrayList<>();
            BigInteger totalVenta = BigInteger.ZERO;
            int numDetalles = faker.number().numberBetween(1, 4); //entre 1 y 3 productos por venta
            for (int j = 0; j < numDetalles; j++) {
                //crear un detalle de venta fake
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta); //relación con la venta principal
                detalle.setProductoId(faker.number().numberBetween(1, 20)); //producto simulado
                int cantidad = faker.number().numberBetween(1, 10); //cantidad de productos
                detalle.setCantidad(cantidad);
                BigInteger precioUnitario = BigInteger.valueOf(faker.number().numberBetween(1000, 10000)); //precio simulado
                detalle.setPrecioUnitario(precioUnitario);
                detalles.add(detalle);
                //sumar al total de la venta
                totalVenta = totalVenta.add(precioUnitario.multiply(BigInteger.valueOf(cantidad)));
            }
            venta.setDetalles(detalles); //asignar detalles a la venta
            venta.setTotal(totalVenta); //asignar el total calculado
            ventaRepository.save(venta); //guardar la venta y sus detalles en la base de datos
        }
    }
}
