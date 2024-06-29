package com.example.spring_mvc_demo.controller;

import com.example.spring_mvc_demo.exception.ResourceNotFoundException;
import com.example.spring_mvc_demo.model.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final List<Producto> productos = new ArrayList<>();

    public ProductoController() {
        productos.add(new Producto(1,"Producto 1",100.0,10));
        productos.add(new Producto(2,"Producto 2",200.0,20));
        productos.add(new Producto(3,"Producto 3",300.0,30));
        productos.add(new Producto(4,"Producto 4",400.0,40));
        productos.add(new Producto(5,"Producto 5",500.0,50));
    }

    //obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    //obtener producto por id
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable int id) {
        Producto producto = productos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        if (producto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(producto, HttpStatus.OK);
    }

    //crear producto
    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        if (producto == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        productos.add(producto);
        return new ResponseEntity<>(producto,HttpStatus.CREATED);
    }

    //actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable int id, @RequestBody Producto productoActualizado) {
        Producto producto = productos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        producto.setNombre(productoActualizado.getNombre());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setCantidad(productoActualizado.getCantidad());
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    //eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable int id) {
        boolean removed = productos.removeIf(producto -> producto.getId() == id);
        if (!removed) {
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<String> convertToInteger(@RequestParam("cantidad") String numberStr) {
        try {
            Integer number = Integer.parseInt(numberStr);
            return ResponseEntity.ok("Numero Convertido: "+ number);
        }
        catch (NumberFormatException ex)
        {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El valor proporcionado no es numero");
        }
    }
}
