package com.jzs.ms_registros_usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jzs.ms_registros_usuarios.model.Usuario;
import com.jzs.ms_registros_usuarios.repository.RegUsuRepository;

@ExtendWith(MockitoExtension.class)
public class RegUsuServiceTest {
    @InjectMocks
    private RegUsuServicelmpl regUsuService;

    @Mock
    private RegUsuRepository regUsuRepositoryMock;

    @Test
    public void crearUsuarioTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("usuario_prueba");
        usuario.setPassword("password_prueba");

        when(regUsuRepositoryMock.save(any())).thenReturn(usuario);

        Usuario usuarioCreado = regUsuService.createUsuario(usuario);

        assertEquals("usuario_prueba", usuarioCreado.getUsername());
        assertEquals("password_prueba", usuarioCreado.getPassword());
    }

    @Test
    public void buscarUsuarioPorUsernameTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("usuario_prueba");
        usuario.setPassword("password_prueba");

        when(regUsuRepositoryMock.findByUsername("usuario_prueba")).thenReturn(usuario);

        Usuario usuarioEncontrado = regUsuService.findByUsername("usuario_prueba");

        assertEquals("usuario_prueba", usuarioEncontrado.getUsername());
        assertEquals("password_prueba", usuarioEncontrado.getPassword());
    }
    
}
