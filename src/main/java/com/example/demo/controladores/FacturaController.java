package com.example.demo.controladores;

import java.util.List;

import com.example.demo.modelos.entidad.ItemFactura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.modelos.entidad.Cliente;
import com.example.demo.modelos.entidad.Factura;
import com.example.demo.modelos.entidad.Producto;
import com.example.demo.servicios.IClienteService;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

//TODO: resolver el null pointer al crear la factura
@Secured("ROLE_ADMIN")
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

	@PostMapping("/form")
	public String guardar(@Valid Factura factura,
						  BindingResult result,
						  Model model,
						  @RequestParam(name = "item_id[]", required = false) Long[] itemId,
						  @RequestParam(name = "cantidad[]", required = false) Long[] cantidad,
						  SessionStatus status) {

		if (result.hasErrors() || itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			return "factura/form";
		}

		for (int i = 0; i < itemId.length; i++){
			Producto producto = clienteService.findProductoById(itemId[i]);

			ItemFactura linea = new ItemFactura();
			linea.setProducto(producto);
			linea.setCantidad(cantidad[i]);

			factura.addItemFactura(linea);
		}
		clienteService.saveFactura(factura);
		status.setComplete();

		return "redirect:/ver/" + factura.getCliente().getId();
	}

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model) {

		Factura factura = clienteService.fetchByIdWithClienteWithItemFacturaWithProducto(id);

		if (factura == null) {
			return "redirect:/listar";
		}

		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));

		return "factura/ver";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id) {
		Factura factura = clienteService.findFacturaById(id);

		if (factura != null) {
			clienteService.deleteFactura(id);
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		return "redirect:/listar";
	}
}
