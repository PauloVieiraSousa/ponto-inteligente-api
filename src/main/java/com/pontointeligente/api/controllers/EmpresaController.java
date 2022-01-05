package com.pontointeligente.api.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pontointeligente.api.dtos.EmpresaDto;
import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.EmpresaService;

@RestController
@RequestMapping(path = "/api/empresas")
@CrossOrigin("*")
public class EmpresaController {

	
	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);
	
	
	@Autowired
	EmpresaService empresaService;
	
	public EmpresaController () {}
	
	
	@GetMapping("/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj (@PathVariable("cnpj") String cnpj){
		log.info("Buscando empresa por cnpj {}", cnpj);
		
		Response<EmpresaDto> response = new Response<EmpresaDto>();
		Optional<Empresa> empresa = empresaService.buscaPorCnpj(cnpj);
		
		if(!empresa.isPresent()) {
			log.info("Empresa não encontrada {}", cnpj);
			response.getErrors().add("Empresa não encontrada para o cnpj" + cnpj);
		}
		
		response.setData(this.converteEmpresaDto(empresa.get()));
		return ResponseEntity.ok(response);
	}
	
	
	
	private EmpresaDto converteEmpresaDto(Empresa empresa) {
		EmpresaDto empresaDto = new EmpresaDto();
		empresaDto.setCnpj(empresa.getCnpj());
		empresaDto.setRazaoSocial(empresa.getRazaoSocial());
		empresaDto.setId(empresa.getId());
		return empresaDto;
	}
	
	

}
