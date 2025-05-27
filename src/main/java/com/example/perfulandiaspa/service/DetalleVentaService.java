package com.example.perfulandiaspa.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.perfulandiaspa.model.DetalleVenta;
import com.example.perfulandiaspa.model.Venta;
import com.example.perfulandiaspa.repository.DetalleVentaRepository;
import com.example.perfulandiaspa.repository.VentaRepository;

@SuppressWarnings("unchecked")
@Service
public class DetalleVentaService {
    //trayendo dependencia necesaria
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private VentaRepository ventaRepository;

    //URLs de microservicios
    @Value("${productos.service.url}")
    private String productoServiceUrl;

    //recibiendo instancia de DetalleVentaRepository como parámetro para asignar al atributo
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    //metodo para obtener todos los detalles de ventas en el sistema
    public List<DetalleVenta> findAll() {
        return detalleVentaRepository.findAll();
    }

    //metodo para obtener todos los detalles de ventas en el sistema
    public List<DetalleVenta> obtenerDetallesVentasCompleto() {
        List<DetalleVenta> detalleVentas = detalleVentaRepository.findAll();

        for (DetalleVenta detalleVenta : detalleVentas) {
            try {
                String productoUrl = productoServiceUrl + "/" + detalleVenta.getProductoId();
                Map<String, Object> productoDetalles = restTemplate.getForObject(productoUrl, Map.class);
                detalleVenta.setProductoDetalles(productoDetalles);

            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        return detalleVentas;
    }

    //metodo para obtener detalles de venta por ID
    public Optional<DetalleVenta> findById(int id) {
        return detalleVentaRepository.findById(id);
    }

    //metodo para obtener un detalle de venta por ID con detalles
    public Optional<DetalleVenta> obtenerDetalleVentaYDetallesProductosPorId(int id) {
        Optional<DetalleVenta> detalleVentaOpt = detalleVentaRepository.findById(id);
        if (detalleVentaOpt.isPresent()) {
            DetalleVenta detalleVenta = detalleVentaOpt.get();
            try {
                String productoUrl = productoServiceUrl + "/" + detalleVenta.getProductoId();
                Map<String, Object> productoDetalles = restTemplate.getForObject(productoUrl, Map.class);
                detalleVenta.setProductoDetalles(productoDetalles);

            } catch (Exception e) {
                throw new RuntimeException("Error al obtener detalles de producto", e);
            }
            return Optional.of(detalleVenta);
        } else {
            return Optional.empty();
        }
    }
    
    //crear detalle de venta
    public DetalleVenta registroDetalleVenta(DetalleVenta detalleVenta) {
        //validacion producto
        String productoUrl = productoServiceUrl + "/" + detalleVenta.getProductoId();

        //control de excepciones al llamar al servicio de productos por si falla
        try {
            Map<String, Object> producto = restTemplate.getForObject(productoUrl, Map.class);

            if (producto == null) {
                throw new RuntimeException("Producto no encontrado");
            }

            BigInteger precio = new BigInteger(producto.get("precio").toString());

            //setear precio de producto a precio unitario
            detalleVenta.setPrecioUnitario(precio);

        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Producto no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al contactar al servicio de productos");
        }
        return detalleVentaRepository.save(detalleVenta);
    }

    //eliminar detalle de venta
    public void eliminarDetalleVenta(int id) {
        //buscar el detalle de venta
        DetalleVenta detalle = detalleVentaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Detalle de Venta con ID " + id + " no encontrado"));

        //obteniendo la venta asociada
        Venta venta = detalle.getVenta();

        //eliminado el detalle de venta
        detalleVentaRepository.deleteById(id);

        //recalcular el total de la venta para evitar inconsistencias
        BigInteger total = BigInteger.ZERO;

        for (DetalleVenta d : venta.getDetalles()) {
            if (d.getId() != id) {
                BigInteger subtotal = d.getPrecioUnitario().multiply(BigInteger.valueOf(d.getCantidad()));
                total = total.add(subtotal);
            }
        }

        //guardando el nuevo total
        venta.setTotal(total);
        ventaRepository.save(venta);
    }

    //editar detalle de venta
    public DetalleVenta actualizarDetalleVenta(int id, DetalleVenta detalleVentaActualizado) {
        DetalleVenta detalleVentaExistente = detalleVentaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Detalle de Venta " + id + " no encontrado"));

        //update
        detalleVentaExistente.setProductoId(detalleVentaActualizado.getProductoId());
        detalleVentaExistente.setCantidad(detalleVentaActualizado.getCantidad());

        //obtener el nuevo precio desde el microservicio de productos
        String productoUrl = productoServiceUrl + "/" + detalleVentaActualizado.getProductoId();

        try {
        Map<String, Object> producto = restTemplate.getForObject(productoUrl, Map.class);

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        BigInteger precio = new BigInteger(producto.get("precio").toString());
        detalleVentaExistente.setPrecioUnitario(precio);

        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Producto no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al contactar al servicio de productos");
        }

        //guardando detalles actualizados
        DetalleVenta actualizado = detalleVentaRepository.save(detalleVentaExistente);

        //actualizando el total de la venta asociada
        Venta venta = actualizado.getVenta();
        BigInteger total = BigInteger.ZERO;

        for (DetalleVenta d : venta.getDetalles()) {
            BigInteger subtotal = d.getPrecioUnitario().multiply(BigInteger.valueOf(d.getCantidad()));
            total = total.add(subtotal);
        }

        venta.setTotal(total);
        ventaRepository.save(venta); //guardando el nuevo total

        return actualizado;
    }
}
