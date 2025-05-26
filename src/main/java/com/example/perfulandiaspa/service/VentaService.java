package com.example.perfulandiaspa.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.perfulandiaspa.model.Venta;
import com.example.perfulandiaspa.repository.VentaRepository;

@Service
public class VentaService {

    //trayendo dependencia necesaria
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private RestTemplate restTemplate;

    //URLs de microservicios
    @Value("${cliente.service.url}")
    private String clienteServiceUrl;

    @Value("${vendedor.service.url}")
    private String vendedorServiceUrl;

    //recibiendo instancia de VentaRepository como parámetro para asignar al atributo
    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    //metodo para obtener todos los ventas
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    //metodo para obtener las ventas (y detalles de la otra db)
    public List<Venta> obtenerVentasYDetalles() {
        List<Venta> ventas = ventaRepository.findAll();

        for (Venta venta : ventas) {
            try {
                String clienteUrl = clienteServiceUrl + "/" + venta.getClienteId();
                Map<String, Object> clienteDetalles = restTemplate.getForObject(clienteUrl, Map.class);
                venta.setClienteDetalles(clienteDetalles);

                String vendedorUrl = vendedorServiceUrl + "/" + venta.getVendedorId();
                Map<String, Object> vendedorDetalles = restTemplate.getForObject(vendedorUrl, Map.class);
                venta.setVendedorDetalles(vendedorDetalles);

            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        return ventas;
    }

    //metodo para obtener una venta por ID
    public Optional<Venta> findById(int id) {
        return ventaRepository.findById(id);
    }

    //metodo para obtener una venta por ID con detalles
    public Optional<Venta> obtenerVentaYDetallesPorId(int id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            try {
                String clienteUrl = clienteServiceUrl + "/" + venta.getClienteId();
                Map<String, Object> clienteDetalles = restTemplate.getForObject(clienteUrl, Map.class);
                venta.setClienteDetalles(clienteDetalles);

                String vendedorUrl = vendedorServiceUrl + "/" + venta.getVendedorId();
                Map<String, Object> vendedorDetalles = restTemplate.getForObject(vendedorUrl, Map.class);
                venta.setVendedorDetalles(vendedorDetalles);

            } catch (Exception e) {
                throw new RuntimeException("Error al obtener detalles de cliente o vendedor", e);
            }
            return Optional.of(venta);
        } else {
            return Optional.empty();
        }
    }

    //crear venta
    public Venta registroVenta(Venta venta) {
        //Validacion cliente
        String clienteUrl = clienteServiceUrl + "/" + venta.getClienteId();

        //control de excepciones al llamar al servicio de cliente por si no está disponible
        try {
            Object cliente = restTemplate.getForObject(clienteUrl, Object.class);

            if (cliente == null) {
                throw new RuntimeException("Cliente no encontrado");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Cliente no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al contactar al servicio de clientes");
        }

        //validacion vendedor
        String vendedorUrl = vendedorServiceUrl + "/" + venta.getVendedorId();

        //control de excepciones al llamar al servicio de vendedor por si no está disponible
        try {
            Object vendedor = restTemplate.getForObject(vendedorUrl, Object.class);

            if (vendedor == null) {
                throw new RuntimeException("Vendedor no encontrado");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Vendedor no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al contactar al servicio de vendedores");
        }

        return ventaRepository.save(venta);
    }

    //eliminar venta
    public void eliminarVenta(int id) {
        ventaRepository.deleteById(id);
    }

    //editar venta
    public Venta actualizarVenta(int id, Venta ventaActualizada) {
        Venta ventaExistente = ventaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Venta con ID " + id + " no encontrada"));

        //Validacion cliente
        String clienteUrl = clienteServiceUrl + "/" + ventaActualizada.getClienteId();

        //control de excepciones al llamar al servicio de cliente por si no está disponible
        try {
            Object cliente = restTemplate.getForObject(clienteUrl, Object.class);

            if (cliente == null) {
                throw new RuntimeException("Cliente no encontrado");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Cliente no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al contactar al servicio de clientes");
        }

        //validacion vendedor
        String vendedorUrl = vendedorServiceUrl + "/" + ventaActualizada.getVendedorId();

        //control de excepciones al llamar al servicio de vendedor por si no está disponible
        try {
            Object vendedor = restTemplate.getForObject(vendedorUrl, Object.class);

            if (vendedor == null) {
                throw new RuntimeException("Vendedor no encontrado");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Vendedor no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al contactar al servicio de vendedores");
        }

        //updates
        ventaExistente.setClienteId(ventaActualizada.getClienteId());
        ventaExistente.setVendedorId(ventaActualizada.getVendedorId());
        ventaExistente.setFecha(ventaActualizada.getFecha());
        ventaExistente.setTotal(ventaActualizada.getTotal());

        return ventaRepository.save(ventaExistente);
    }
}