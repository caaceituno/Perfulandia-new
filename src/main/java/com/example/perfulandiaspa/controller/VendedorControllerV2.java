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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.perfulandiaspa.assembler.VendedorModelAssembler;
import com.example.perfulandiaspa.model.Vendedor;
import com.example.perfulandiaspa.service.VendedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v2/vendedores")
public class VendedorControllerV2 {

    @Autowired
    private VendedorService vendedorService;

    @Autowired
    private VendedorModelAssembler assembler;

    // obtener todos los vendedores
    @Operation(summary = "Listar todos los vendedores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendedores obtenidos correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Vendedor>>> listarVendedores() {
        try {
            List<Vendedor> vendedores = vendedorService.findAll();
            List<EntityModel<Vendedor>> vendedoresModel = vendedores.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            return ResponseEntity.ok(CollectionModel.of(vendedoresModel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener un vendedor por ID
    @Operation(summary = "Obtener un vendedor por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendedor encontrado"),
        @ApiResponse(responseCode = "404", description = "Vendedor no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVendedorPorId(@PathVariable int id) {
        try {
            Optional<Vendedor> vendedor = vendedorService.findById(id);
            if (vendedor.isPresent()) {
                return ResponseEntity.ok(assembler.toModel(vendedor.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendedor no encontrado");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //crear un nuevo vendedor
    @Operation(summary = "Registrar un nuevo vendedor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Vendedor registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<?> registroCompleto(@RequestBody Vendedor vendedor) {
        try {
            Vendedor nuevoVendedor = vendedorService.registroVendedor(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(nuevoVendedor));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    //eliminar un vendedor por ID
    @Operation(summary = "Eliminar un vendedor por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Vendedor eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Vendedor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVendedor(@PathVariable int id) {
        try {
            vendedorService.eliminarVendedor(id);
            return ResponseEntity.noContent().build(); // 204 si sale bien
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage())); //404 si no se encuentra el vendedor
        }
    }

    //editar un vendedor por ID (PUT)
    @Operation(summary = "Actualizar un vendedor por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendedor actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVendedor(@PathVariable int id, @RequestBody Vendedor vendedor) {
        try {
            Vendedor vendedorActualizado = vendedorService.actualizarVendedor(id, vendedor);
            return ResponseEntity.ok(assembler.toModel(vendedorActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }  
    }
}