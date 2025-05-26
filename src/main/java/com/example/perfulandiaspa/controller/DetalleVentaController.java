package com.example.perfulandiaspa.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.perfulandiaspa.model.DetalleVenta;
import com.example.perfulandiaspa.service.DetalleVentaService;

@RestController
@RequestMapping("api/v1/detalle-ventas")
public class DetalleVentaController {
    @Autowired
    private DetalleVentaService detalleVentaService;

    //obtener todos los detalles de ventas
    @GetMapping
    public ResponseEntity<?> listarDetalleVentas() {
        try {
            List<DetalleVenta> detalleVentas = detalleVentaService.findAll();
            if (detalleVentas.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay ventas registradas");
            }
            return ResponseEntity.ok(detalleVentas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener todos los detalles de ventas con datos completos
    @GetMapping("/all")
    public ResponseEntity<?> obtenerDetallesVentasCompleto() {
        try {
            List<DetalleVenta> detalleVentas = detalleVentaService.obtenerDetallesVentasCompleto();
            if (detalleVentas.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay ventas registradas");
            }
            return ResponseEntity.ok(detalleVentas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVentaPorId(@PathVariable int id) {
        try {
            Optional<DetalleVenta> detalleVenta = detalleVentaService.findById(id);
            if (detalleVenta.isPresent()) {
                return ResponseEntity.ok(detalleVenta.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle de enta no encontrada");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener detalles de ventas por ID completo
    @GetMapping("/detalles/{id}")
    public ResponseEntity<?> obtenerDetalleVentaYDetallesProductosPorId(@PathVariable int id) {
        try {
            Optional<DetalleVenta> detalleVentaCompleto = detalleVentaService.obtenerDetalleVentaYDetallesProductosPorId(id);
            if (detalleVentaCompleto.isPresent()) {
                return ResponseEntity.ok(detalleVentaCompleto.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //crear una nueva venta
    @PostMapping
    public ResponseEntity<?> registroDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        try {
            DetalleVenta nuevoDetalleVenta = detalleVentaService.registroDetalleVenta(detalleVenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalleVenta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    //eliminar una venta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDetalleVenta(@PathVariable int id) {
        try {
            detalleVentaService.eliminarDetalleVenta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } 
    }

    //editar una venta por ID (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDetalleVenta(@PathVariable int id, @RequestBody DetalleVenta detalleVenta) {
        try {
            detalleVenta.setId(id);
            DetalleVenta detalleVentaActualizada = detalleVentaService.actualizarDetalleVenta(id, detalleVenta);
            return ResponseEntity.ok(detalleVentaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }  
    }
}
