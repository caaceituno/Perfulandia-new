package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.service.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
public class RolController {
    @Autowired
    private RolService rolService;

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
    public ResponseEntity<List<Rol>> listarRoles() {
        List<Rol> roles = rolService.findAll();
        return ResponseEntity.ok(roles);
    }

}