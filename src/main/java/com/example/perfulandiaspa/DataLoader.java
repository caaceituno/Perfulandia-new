package com.example.perfulandiaspa;

import com.example.perfulandiaspa.model.*;
import com.example.perfulandiaspa.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendedorRepository vendedorRepository;

    private Faker faker = new Faker();

    @Override
    public void run(String... args) throws Exception {
        // 1. Crear roles base (si no existen)
        Rol rolAdmin = getOrCreateRol("ADMIN");
        Rol rolVendedor = getOrCreateRol("VENDEDOR");
        Rol rolCliente = getOrCreateRol("CLIENTE");

        // 2. Crear usuarios y asignar roles con datos fake

        // Crear algunos admins
        for (int i = 0; i < 3; i++) {
            String username = "admin" + i;
            if (usuarioRepository.findByUsername(username) == null) {
                Usuario admin = new Usuario();
                admin.setUsername(username);
                admin.setPassword("admin123"); // en real usarías hash
                admin.setEmail(faker.internet().emailAddress());
                admin.setEnabled(true);
                admin.setRol(rolAdmin);
                usuarioRepository.save(admin);
            }
        }

        // Crear vendedores
        for (int i = 0; i < 5; i++) {
            String username = "vendedor" + i;
            if (usuarioRepository.findByUsername(username) == null) {
                Usuario vendedorUser = new Usuario();
                vendedorUser.setUsername(username);
                vendedorUser.setPassword("vendedor123");
                vendedorUser.setEmail(faker.internet().emailAddress());
                vendedorUser.setEnabled(true);
                vendedorUser.setRol(rolVendedor);
                // Guardar y obtener la instancia gestionada
                vendedorUser = usuarioRepository.save(vendedorUser);

                Vendedor vendedor = new Vendedor();
                vendedor.setUsuario(vendedorUser);
                vendedor.setNombreCompleto(faker.name().fullName());
                vendedor.setSucursal(faker.company().name());
                vendedor.setMetaMensual((float) faker.number().randomDouble(2, 1000, 10000));
                vendedorRepository.save(vendedor);
            }
        }

        // Crear clientes
        for (int i = 0; i < 10; i++) {
            String username = "cliente" + i;
            if (usuarioRepository.findByUsername(username) == null) {
                Usuario clienteUser = new Usuario();
                clienteUser.setUsername(username);
                clienteUser.setPassword("cliente123");
                clienteUser.setEmail(faker.internet().emailAddress());
                clienteUser.setEnabled(true);
                clienteUser.setRol(rolCliente);
                // Guardar y obtener la instancia gestionada
                clienteUser = usuarioRepository.save(clienteUser);

                Cliente cliente = new Cliente();
                cliente.setUsuario(clienteUser);
                cliente.setNombreCompleto(faker.name().fullName());
                cliente.setRut(generarRutFake());
                cliente.setTelefono(faker.phoneNumber().phoneNumber());
                cliente.setDireccion(faker.address().fullAddress());
                clienteRepository.save(cliente);
            }
        }
    }

    private Rol getOrCreateRol(String nombre) {
        Rol rol = rolRepository.findByNombre(nombre);
        if (rol == null) {
            rol = new Rol();
            rol.setNombre(nombre);
            rol = rolRepository.save(rol);
        }
        return rol;
    }

    // Genera un RUT chileno fake básico, no válido para producción pero sirve para pruebas
    private String generarRutFake() {
        int numero = faker.number().numberBetween(10000000, 25000000);
        char dv = calcularDv(numero);
        return numero + "-" + dv;
    }

    // Calcula dígito verificador básico para el rut
    private char calcularDv(int rut) {
        int suma = 0;
        int mul = 2;
        while (rut > 0) {
            suma += (rut % 10) * mul;
            rut /= 10;
            mul = (mul == 7) ? 2 : mul + 1;
        }
        int res = 11 - (suma % 11);
        if (res == 11) return '0';
        if (res == 10) return 'K';
        return (char) (res + '0');
    }
}
