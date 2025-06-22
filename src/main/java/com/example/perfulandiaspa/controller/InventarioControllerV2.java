package com.example.perfulandiaspa.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.perfulandiaspa.assembler.InventarioModelAssembler;
import com.example.perfulandiaspa.model.Inventario;
import com.example.perfulandiaspa.service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v2/inventario")
public class InventarioControllerV2 {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private InventarioModelAssembler assembler;

    //obtener todos los inventarios
    @Operation(summary = "Listar todos los inventarios")
    @ApiResponse(responseCode = "200", description = "Inventarios listados correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Inventario>>> listarInventarios() {
        try {
            List<Inventario> inventarios = inventarioService.findAll();
            List<EntityModel<Inventario>> inventariosModel = inventarios.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(inventariosModel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener inventario por id
    @Operation(summary = "Obtener inventario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario encontrado"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getInventarioById(@PathVariable int id) {
        try {
            Inventario inventario = inventarioService.findById(id);
            if (inventario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Inventario con ID " + id + " no encontrado"));
            }
            return ResponseEntity.ok(assembler.toModel(inventario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //crear nuevo inventario
    @Operation(summary = "Registrar nuevo inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Inventario registrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<?> registroInventario(@RequestBody Inventario inventario) {
        try {
            Inventario nuevoInventario = inventarioService.registroInventario(inventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(nuevoInventario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    //borrar inventario por id
    @Operation(summary = "Eliminar inventario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Inventario eliminado"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInventario(@PathVariable int id) {
        try {
            inventarioService.eliminarInventario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } 
    }
    
    //actualizar inventario
    @Operation(summary = "Actualizar inventario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInventario(@PathVariable int id, @RequestBody Inventario inventario) {
        try {
            Inventario inventarioActualizado = inventarioService.actualizarInventario(id, inventario);
            return ResponseEntity.ok(assembler.toModel(inventarioActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }  
    }
}