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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v1/detalle-ventas")
public class DetalleVentaController {
    @Autowired
    private DetalleVentaService detalleVentaService;

    //obtener todos los detalles de ventas
    @Operation(summary = "Listar todos los detalles de ventas")
    @ApiResponse(responseCode = "200", description = "Detalles listados correctamente")
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
    @Operation(summary = "Listar todos los detalles de ventas con datos completos (cliente, vendedor, productos)")
    @ApiResponse(responseCode = "200", description = "Detalles completos obtenidos correctamente")
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
    @Operation(summary = "Obtener detalle de venta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle encontrado"),
        @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
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
    @Operation(summary = "Obtener detalle completo de una venta específica (cliente, vendedor, productos)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle completo encontrado"),
        @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    @GetMapping("/{id}/detalles")
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
    @Operation(summary = "Registrar un nuevo detalle de venta")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Detalle registrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
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
    @Operation(summary = "Eliminar un detalle de venta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Detalle eliminado"),
        @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
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
    @Operation(summary = "Actualizar un detalle de venta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle actualizado"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar")
    })
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
