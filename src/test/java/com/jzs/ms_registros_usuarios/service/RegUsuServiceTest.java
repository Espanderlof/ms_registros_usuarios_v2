package com.jzs.ms_registros_usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    public void actualizarUsuarioTest() {
        Long usuarioId = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(usuarioId);
        usuarioExistente.setUsername("usuario_existente");
        usuarioExistente.setPassword("password_existente");
    
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(usuarioId);
        usuarioActualizado.setUsername("usuario_actualizado");
        usuarioActualizado.setPassword("password_actualizado");
    
        when(regUsuRepositoryMock.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(regUsuRepositoryMock.save(any())).thenReturn(usuarioActualizado);
    
        Usuario resultado = regUsuService.updateUsuario(usuarioId, usuarioActualizado);
    
        assertEquals(usuarioId, resultado.getId());
        assertEquals("usuario_actualizado", resultado.getUsername());
        assertEquals("password_actualizado", resultado.getPassword());
    }

    @Test
    public void eliminarUsuarioTest() {
        Long usuarioId = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(usuarioId);
        usuarioExistente.setUsername("usuario_eliminar");
        usuarioExistente.setPassword("password_eliminar");
    
        when(regUsuRepositoryMock.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
    
        // Verificar que se busque el usuario por su ID antes de eliminarlo
        Usuario usuarioEncontrado = regUsuService.getUsuarioById(usuarioId).orElse(null);
        assertEquals(usuarioExistente, usuarioEncontrado);
    
        regUsuService.deleteUsuario(usuarioId);
    
        verify(regUsuRepositoryMock, times(1)).deleteById(usuarioId);
    }
    
}
