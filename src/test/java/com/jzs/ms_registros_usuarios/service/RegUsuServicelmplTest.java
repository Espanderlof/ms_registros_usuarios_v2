package com.jzs.ms_registros_usuarios.service;

import com.jzs.ms_registros_usuarios.model.Usuario;
import com.jzs.ms_registros_usuarios.repository.RegUsuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegUsuServicelmplTest {
    @InjectMocks
    private RegUsuServicelmpl regUsuService;

    @Mock
    private RegUsuRepository regUsuRepository;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    public void setUp() {
        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("usuario1");
        usuario1.setPassword("password1");

        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("usuario2");
        usuario2.setPassword("password2");
    }
    
    @Test
    public void getAllUsuariosTest() {
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(regUsuRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = regUsuService.getAllUsuarios();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(usuario1));
        assertTrue(resultado.contains(usuario2));
    }

    @Test
    public void getUsuarioByIdTest() {
        when(regUsuRepository.findById(1L)).thenReturn(Optional.of(usuario1));

        Optional<Usuario> resultado = regUsuService.getUsuarioById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(usuario1, resultado.get());
    }

    @Test
    public void createUsuarioTest() {
        when(regUsuRepository.save(any(Usuario.class))).thenReturn(usuario1);

        Usuario resultado = regUsuService.createUsuario(usuario1);

        assertEquals(usuario1, resultado);
    }

    @Test
    public void updateUsuarioTest() {
        when(regUsuRepository.findById(1L)).thenReturn(Optional.of(usuario1));
        when(regUsuRepository.save(any(Usuario.class))).thenReturn(usuario1);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setUsername("usuario1_actualizado");
        usuarioActualizado.setPassword("password1_actualizado");

        Usuario resultado = regUsuService.updateUsuario(1L, usuarioActualizado);

        assertEquals(usuario1.getId(), resultado.getId());
        assertEquals(usuarioActualizado.getUsername(), resultado.getUsername());
        assertEquals(usuarioActualizado.getPassword(), resultado.getPassword());
    }
    
}
