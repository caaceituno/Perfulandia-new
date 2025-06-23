package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.assembler.DetalleVentaModelAssembler;
import com.example.perfulandiaspa.model.DetalleVenta;
import com.example.perfulandiaspa.service.DetalleVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/detalle-ventas")
public class DetalleVentaControllerV2 {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private DetalleVentaModelAssembler assembler;

    //listar todos los detalles de venta
    @Operation(summary = "Listar todos los detalles de venta")
    @ApiResponse(responseCode = "200", description = "Detalles de venta listados correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<DetalleVenta>>> listarDetalleVentas() {
        try {
            List<DetalleVenta> detalles = detalleVentaService.findAll();
            List<EntityModel<DetalleVenta>> detallesModel = detalles.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(detallesModel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener detalle de venta por id
    @Operation(summary = "Obtener detalle de venta por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de venta encontrado"),
            @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetalleVentaPorId(@PathVariable int id) {
        try {
            Optional<DetalleVenta> detalle = detalleVentaService.findById(id);
            if (detalle.isPresent()) {
                return ResponseEntity.ok(assembler.toModel(detalle.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle de venta no encontrado");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //crear nuevo detalle de venta
    @Operation(summary = "Registrar nuevo detalle de venta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Detalle de venta registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<?> registroDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        try {
            DetalleVenta nuevoDetalle = detalleVentaService.registroDetalleVenta(detalleVenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(nuevoDetalle));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    //eliminar detalle de venta por id
    @Operation(summary = "Eliminar detalle de venta por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Detalle de venta eliminado"),
            @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado")
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

    //actualizar detalle de venta
    @Operation(summary = "Actualizar detalle de venta por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de venta actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDetalleVenta(@PathVariable int id, @RequestBody DetalleVenta detalleVenta) {
        try {
            DetalleVenta actualizado = detalleVentaService.actualizarDetalleVenta(id, detalleVenta);
            return ResponseEntity.ok(assembler.toModel(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
