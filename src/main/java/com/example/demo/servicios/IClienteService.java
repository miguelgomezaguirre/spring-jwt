package com.example.demo.servicios;

import java.util.List;

import com.example.demo.modelos.entidad.Cliente;
import com.example.demo.modelos.entidad.Factura;
import com.example.demo.modelos.entidad.Producto;

public interface IClienteService {

	public List<Cliente> findAll();

	public void save(Cliente cliente);

	public Cliente findOne(Long id);
	
	public void delete(Long id);
	
	public List<Producto> findByNombre(String term);

	void saveFactura(Factura factura);

	Producto findProductoById(Long id);

	Factura findFacturaById(Long id);
}
