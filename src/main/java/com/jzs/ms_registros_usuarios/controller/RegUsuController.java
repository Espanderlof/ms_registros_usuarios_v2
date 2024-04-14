package com.jzs.ms_registros_usuarios.controller;

import com.jzs.ms_registros_usuarios.model.Usuario;
import com.jzs.ms_registros_usuarios.service.RegUsuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.validation.FieldError;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/usuarios")
public class RegUsuController {

    private static final Logger log = LoggerFactory.getLogger(RegUsuController.class);

    @Autowired
    private RegUsuService regUsuService;  
    
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = regUsuService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@Validated @PathVariable Long id) {
        if (id == null) {
            log.info("Debe ingresar el id de usuario.");
            return ResponseEntity.badRequest().body(new ErrorResponse(false, "Debe ingresar el id de usuario."));
        }

        Optional<Usuario> usuario = regUsuService.getUsuarioById(id);
        if (!usuario.isPresent()) {
            log.info("No se encontro el usuario con ID {}.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(false, "No se encontro el usuario con ID "+id+"."));
        }
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario nuevaUsuario = regUsuService.createUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioOptional = regUsuService.getUsuarioById(id);
        if (!usuarioOptional.isPresent()) {
            log.info("No se encontro el usuario con ID {}.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(false, "No se encontro el usuario con ID "+id+"."));
        }

        Usuario usuarioActualizado = regUsuService.updateUsuario(id, usuario);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@Validated @PathVariable Long id) {
        if (id == null) {
            log.info("Debe ingresar el id de usuario a eliminar.");
            return ResponseEntity.badRequest().body(new ErrorResponse(false, "Debe ingresar el id de usuario a eliminar."));
        }

        Optional<Usuario> usuario = regUsuService.getUsuarioById(id);
        if (!usuario.isPresent()) {
            log.info("No se encontro el usuario con ID {}.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(false, "No se encontro el usuario con ID "+id+"."));
        }

        // regUsuService.deleteUsuario(id);
        // return ResponseEntity.noContent().build();
        
        try {
            regUsuService.deleteUsuario(id);
            return ResponseEntity.ok(new ErrorResponse(true, "Usuario eliminado correctamente."));
        } catch (Exception e) {
            log.error("Error al eliminar el usuario con ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(false, "Ocurrió un error al eliminar el usuario."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> autenticarUsuario(@RequestBody(required = false) Map<String, String> credentials) {
        //validar que vengan datos (json) en el post. ya que si se deba vacio da un error muy grande de la excepcion
        if (credentials == null || credentials.isEmpty()) {
            log.info("Debe ingresar el usuario y la contrasena.");
            return ResponseEntity.badRequest().body(new ErrorResponse(false, "Debe ingresar el usuario y la contraseña."));
        }

        String username = credentials.get("username");
        String password = credentials.get("password");

        //validar que se ingrese el usuario y contraseña
        if (username == null || password == null) {
            log.info("Debe ingresar el usuario y la contrasena.");
            return ResponseEntity.badRequest().body(new ErrorResponse(false, "Debe ingresar el usuario y la contraseña."));
        }

        //buscar el usuario por nomber de usuario
        Usuario usuario = regUsuService.findByUsername(username);

        //validar si el usuario existe
        if (usuario == null) {
            log.info("El usuario no existe.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(false, "El usuario no existe."));
        }

        //validar si la contraseña es correcta
        if (!password.equals(usuario.getPassword())) {
            log.info("Contrasena incorrecta.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(false, "Contraseña incorrecta."));
        }

        //si esta todo correcto lanzamos el true
        log.info("Autenticacion exitosa.");
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

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorResponse(false, "El ID no puede ser nulo o texto.");
    }  
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
