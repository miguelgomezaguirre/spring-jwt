package com.example.demo.modelos.dao;

import com.example.demo.modelos.entidad.Factura;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {
}
