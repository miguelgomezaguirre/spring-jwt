package com.example.demo.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.modelos.entidad.Cliente;
import com.example.demo.modelos.entidad.Factura;
import com.example.demo.modelos.entidad.Producto;
import com.example.demo.servicios.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	@RequestMapping(value="/form/{clienteId}")
	private String crear(@PathVariable(value="clienteId") Long clienteId, Model model) {
		Cliente cliente = clienteService.findOne(clienteId);
		
		if (cliente==null) {
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Crear Factura");
		
		return "factura/form";
	}
	
	@GetMapping(value="/cargar-productos/{term}", produces = {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return clienteService.findByNombre(term);
	}
}
