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