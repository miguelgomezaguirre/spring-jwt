package com.example.demo.modelos.dao;

import com.example.demo.modelos.entidad.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    Usuario findByUsername(String username);

}
