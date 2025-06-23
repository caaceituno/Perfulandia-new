package com.example.perfulandiaspa.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.perfulandiaspa.assembler.VentaModelAssembler;
import com.example.perfulandiaspa.model.Venta;
import com.example.perfulandiaspa.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v2/ventas")
public class VentaControllerV2 {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaModelAssembler assembler;

    //obtener todas las ventas
    @Operation(summary = "Listar todas las ventas")
    @ApiResponse(responseCode = "200", description = "Ventas listadas correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Venta>>> listarVentas() {
        try {
            List<Venta> ventas = ventaService.findAll();
            List<EntityModel<Venta>> ventasModel = ventas.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(ventasModel));
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
                return ResponseEntity.ok(assembler.toModel(venta.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada");
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
            return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(nuevaVenta));
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
            return ResponseEntity.ok(assembler.toModel(ventaActualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }  
    }
}