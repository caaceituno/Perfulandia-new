package com.example.perfulandiaspa.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.perfulandiaspa.model.Venta;
import com.example.perfulandiaspa.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v1/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    //obtener todas las ventas
    @Operation(summary = "Listar todas las ventas")
    @ApiResponse(responseCode = "200", description = "Ventas listadas correctamente")
    @GetMapping
    public ResponseEntity<?> listarVentas() {
        try {
            List<Venta> ventas = ventaService.findAll();
            if (ventas.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay ventas registradas");
            }
            return ResponseEntity.ok(ventas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener todas las ventas con detalles
    @Operation(summary = "Listar todas las ventas con sus detalles")
    @ApiResponse(responseCode = "200", description = "Ventas con detalles listadas correctamente")
    @GetMapping("/detalles")
    public ResponseEntity<?> listarVentasYDetalles() {
        try {
            List<Venta> ventas = ventaService.obtenerVentasYDetalles();
            if (ventas.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay ventas registradas");
            }
            return ResponseEntity.ok(ventas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener venta por ID
    @Operation(summary = "Obtener venta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta encontrada"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVentaPorId(@PathVariable int id) {
        try {
            Optional<Venta> venta = ventaService.findById(id);
            if (venta.isPresent()) {
                return ResponseEntity.ok(venta.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener ventas por ID con detalles
    @Operation(summary = "Obtener venta por ID con detalles (cliente, vendedor, productos)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta con detalles encontrada"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/detalles/{id}")
    public ResponseEntity<?> obtenerVentaConDetallesPorId(@PathVariable int id) {
        try {
            Optional<Venta> ventaConDetalles = ventaService.obtenerVentaYDetallesPorId(id);
            if (ventaConDetalles.isPresent()) {
                return ResponseEntity.ok(ventaConDetalles.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //crear una nueva venta
    @Operation(summary = "Registrar una nueva venta")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Venta registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al registrar la venta")
    })
    @PostMapping
    public ResponseEntity<?> registroVenta(@RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.registroVenta(venta);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
    
    //eliminar una venta por ID
    @Operation(summary = "Eliminar una venta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Venta eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable int id) {
        try {
            ventaService.eliminarVenta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } 
    }

    //editar una venta por ID (PUT)
    @Operation(summary = "Actualizar una venta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar la venta")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable int id, @RequestBody Venta venta) {
        try {
            venta.setId(id);
            Venta ventaActualizada = ventaService.actualizarVenta(id, venta);
            return ResponseEntity.ok(ventaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }  
    }
}