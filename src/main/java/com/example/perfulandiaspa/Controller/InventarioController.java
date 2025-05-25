// package com.example.perfulandiaspa.Controller;
//
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.example.perfulandiaspa.model.Inventario;
// import com.example.perfulandiaspa.service.InventarioService;
//
// @RestController
// @RequestMapping("/inventario")
// public class InventarioController {
//
//
//     @GetMapping ("/prueba")
//     public String mensajePrueba() {
//         return "El inventario se muestra correctamente.";
//     }   
//
//     @GetMapping("/demo")
//     public String productoDemo() {
//
//         int id = 1; // Cambia este ID si es necesario
//         Inventario inventario = inventarioService.obtenerId(id);
//         if (inventario == null) {
//             return "No se encontró producto con esa ID: " + id;
//         }
//
//         Inventario inventarioSimulado = new Inventario();
//         inventarioSimulado.setId(id);
//         inventarioSimulado.setProductoId(234);
//         inventarioSimulado.setCantidad(5);
//         inventarioSimulado.setUbicacion("Almacen A");
//
//         return String.format(
//             "INVENTARIO\n=======\nId: %s\nNombre: %.2f\n\nCantidad: ",
//             inventarioSimulado.getId(),
//             inventarioSimulado.getProductoId(),
//             inventarioSimulado.getCantidad(),
//             inventarioSimulado.getUbicacion()
//         );
//     }
//
//     @Autowired
//     private InventarioService inventarioService;
//
//     @GetMapping("/inventario1")
//     public String pruebaInventario() {
//
//         int id = 1; // Cambia este ID si es necesario
//         Inventario inventario = inventarioService.obtenerId(id);
//         if (inventario == null) {
//             return "No se encontró producto con la Id " + id;
//         }
//
//
//         return String.format(
//             "PRODUCTO\n=======\nId: %s\nIdProducto: %d\nCantidad: %d",
//             inventario.getId(),
//             inventario.getProductoId(),
//             inventario.getCantidad(),
//             inventario.getUbicacion()
//         );
//         }
//     }

package com.example.perfulandiaspa.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.perfulandiaspa.model.Inventario;
import com.example.perfulandiaspa.service.InventarioService;

@RestController
@RequestMapping("api/v1/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Obtener todo el inventario
    @GetMapping
    public ResponseEntity<List<Inventario>> listarInventario() {
        try {
            List<Inventario> inventario = inventarioService.findAll();
            return ResponseEntity.ok(inventario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Registrar nuevo inventario
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarInventario(@RequestBody Inventario inventario) {
        try {
            Inventario nuevoInventario = inventarioService.guardarInventario(inventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // Eliminar inventario por ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarInventario(@PathVariable int id) {
        try {
            inventarioService.eliminarInventario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // Actualizar inventario por ID
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> actualizarInventario(@PathVariable int id, @RequestBody Inventario inventario) {
        try {
            inventario.setId(id);
            Inventario inventarioActualizado = inventarioService.actualizarInventario(inventario);
            return ResponseEntity.ok(inventarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}