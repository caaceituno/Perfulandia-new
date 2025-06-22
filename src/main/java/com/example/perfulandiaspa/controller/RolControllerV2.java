package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.assembler.RolModelAssembler;
import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.service.RolService;

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
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v2/roles")
public class RolControllerV2 {
    @Autowired
    private RolService rolService;

    @Autowired
    private RolModelAssembler assembler;

    // obtener todos los roles
    @GetMapping
    @Operation(
        summary = "Listar todos los roles",
        description = "Obtiene una lista con todos los roles registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Rol>>> listarRoles() {
        try {
            List<Rol> roles = rolService.findAll();
            List<EntityModel<Rol>> rolesModel = roles.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            return ResponseEntity.ok(CollectionModel.of(rolesModel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}