package com.jzs.ms_registros_usuarios.controller;

import com.jzs.ms_registros_usuarios.model.Usuario;
import com.jzs.ms_registros_usuarios.service.RegUsuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/usuarios")
public class RegUsuController {

    @Autowired
    private RegUsuService regUsuService;  
    
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = regUsuService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = regUsuService.getUsuarioById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        Usuario nuevaUsuario = regUsuService.createUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = regUsuService.updateUsuario(id, usuario);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        regUsuService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> autenticarUsuario(@RequestBody(required = false) Map<String, String> credentials) {
        //validar que vengan datos (json) en el post. ya que si se deba vacio da un error muy grande de la excepcion
        if (credentials == null || credentials.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(false, "Debe ingresar el usuario y la contraseña."));
        }

        String username = credentials.get("username");
        String password = credentials.get("password");

        //validar que se ingrese el usuario y contraseña
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(false, "Debe ingresar el usuario y la contraseña."));
        }

        //buscar el usuario por nomber de usuario
        Usuario usuario = regUsuService.findByUsername(username);

        //validar si el usuario existe
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(false, "El usuario no existe."));
        }

        //validar si la contraseña es correcta
        if (!password.equals(usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(false, "Contraseña incorrecta."));
        }

        //si esta todo correcto lanzamos el true
        return ResponseEntity.ok(new ErrorResponse(true, "Autenticación exitosa."));
    }

    static class ErrorResponse {
        private final boolean respuesta;
        private final String message;
    
        public ErrorResponse(boolean respuesta, String message) {
            this.respuesta = respuesta;
            this.message = message;
        }
    
        public boolean isRespuesta() {
            return respuesta;
        }
    
        public String getMessage() {
            return message;
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(false, "Sin datos ingresados.");
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
