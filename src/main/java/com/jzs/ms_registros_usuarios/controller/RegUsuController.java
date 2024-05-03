package com.jzs.ms_registros_usuarios.controller;

import com.jzs.ms_registros_usuarios.model.Usuario;
import com.jzs.ms_registros_usuarios.service.RegUsuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    public CollectionModel<EntityModel<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = regUsuService.getAllUsuarios();
        log.info("GET /usuarios");
        log.info("Retornando todos los usuarios");
        List<EntityModel<Usuario>> usuariosResources = usuarios.stream()
                .map(usuario -> EntityModel.of(usuario,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(usuario.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios());
        CollectionModel<EntityModel<Usuario>> resources = CollectionModel.of(usuariosResources, linkTo.withRel("usuarios"));

        return resources;
    }

    @GetMapping("/{id}")
    public EntityModel<Usuario> getUsuarioById(@Validated @PathVariable Long id) {
        Optional<Usuario> usuario = regUsuService.getUsuarioById(id);

        if (usuario.isPresent()) {
            return EntityModel.of(usuario.get(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios()).withRel("all-usuarios"));
        } else {
            throw new RegUsuNotFoundException("No se encontró el usuario con ID " + id);
        }
    }

    @PostMapping
    public EntityModel<Usuario> createUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = regUsuService.createUsuario(usuario);
        return EntityModel.of(nuevoUsuario,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(nuevoUsuario.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios()).withRel("all-usuarios"));
    }

    @PutMapping("/{id}")
    public EntityModel<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = regUsuService.updateUsuario(id, usuario);
        return EntityModel.of(usuarioActualizado,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios()).withRel("all-usuarios"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@Validated @PathVariable Long id) {
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

    static class RegUsuNotFoundException extends RuntimeException {
        public RegUsuNotFoundException(String message) {
            super(message);
        }
    }
}
