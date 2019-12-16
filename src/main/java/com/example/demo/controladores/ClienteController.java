package com.example.demo.controladores;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.modelos.entidad.Cliente;
import com.example.demo.servicios.IClienteService;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	private final static String UPLOAD_FOLDER = "uploads";

	@RequestMapping(value = {"/listar", "/"})
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clienteService.findAll());
		return "listar";
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String crear(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("titulo", "Datos del cliente");
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
	public String editar(@PathVariable(value = "id") Long id, Model model) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
		} else {
			return "redirect:/listar";
		}

		model.addAttribute("titulo", "Editar cliente");
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Datos del cliente");
			return "form";
		}

		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {

				Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
				File archivo = rootPath.toFile();

				if (archivo.exists() && archivo.canRead()) {
					archivo.delete();
				}
			}

			String uniqueFileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
			Path rootAbsolutePath = Paths.get(UPLOAD_FOLDER).resolve(uniqueFileName).toAbsolutePath();
			try {
				Files.copy(foto.getInputStream(), rootAbsolutePath);
				cliente.setFoto(uniqueFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		clienteService.save(cliente);
		return "redirect:listar";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);

			Path rootPath = Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
			File archivo = rootPath.toFile();

			if (archivo.exists() && archivo.canRead()) {
				archivo.delete();
			}
		}
		return "redirect:/listar";
	}

	@RequestMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model) {
		Cliente cliente = clienteService.fetchByIdWithFacturas(id);
		if (cliente == null) {
			return "redirect:listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Datos del cliente: " + cliente.getNombre() + " " + cliente.getApellido());
		return "ver";
	}
}
