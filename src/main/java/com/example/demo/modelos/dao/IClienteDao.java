package com.example.demo.modelos.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.modelos.entidad.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{

}
