package com.example.demo.modelos.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.modelos.entidad.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{

    @Query("select c from Cliente c " +
            "left join fetch c.facturas " +
            "where c.id = ?1")
    Cliente fetchByIdWithFacturas(Long id);
}
