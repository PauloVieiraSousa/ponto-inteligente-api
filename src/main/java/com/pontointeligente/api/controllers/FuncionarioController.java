package com.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pontointeligente.api.dtos.FuncionarioDto;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.FuncionarioService;
import com.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping(path = "/api/funcionarios")
@CrossOrigin("*")
public class FuncionarioController {

	private static Logger log = LoggerFactory.getLogger(FuncionarioController.class);
	
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	public FuncionarioController() {}
	

	@PutMapping(path="/{id}")
	public ResponseEntity<Response<FuncionarioDto>> atualiza(
			@PathParam("id") Long id, 
			@Valid @RequestBody FuncionarioDto funcionarioDto, 
			BindingResult result )
			throws NoSuchAlgorithmException
	{
		log.info("Atualizando funcionario {}", funcionarioDto.toString());
		
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		
		Optional<Funcionario> funcionario = funcionarioService.buscarPorId(id);
		
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não encontrado."));
		}
		
		this.atualizaDadosDoFuncionario(funcionario.get(), funcionarioDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando funcionario {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.funcionarioService.persistir(funcionario.get());
		response.setData(this.converteFuncionarioDto(funcionario.get()));
		
		return ResponseEntity.ok(response);		
	}
	
	
	
	private void atualizaDadosDoFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult result) throws NoSuchAlgorithmException {
		
		if(!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
			this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
			.ifPresent(func-> result.addError(new ObjectError("funcionario", "Email já existente.")));
			funcionario.setEmail(funcionarioDto.getEmail());
		}
		
		funcionario.setQtdHorasAlmoco(null);
		
		funcionarioDto.getQtdHorasTrabalhoDia()
		.ifPresent( qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		
		funcionario.setValorHora(null);
		
		funcionarioDto.getValorHora()
		.ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

		if(funcionarioDto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha().get()));
			}
	
	}
	
	
	private FuncionarioDto converteFuncionarioDto (Funcionario funcionario) {
		FuncionarioDto funcionarioDto = new FuncionarioDto();
		funcionarioDto.setEmail(funcionario.getEmail());
		funcionarioDto.setId(funcionario.getId());
		funcionarioDto.setNome(funcionario.getNome());
	
		return funcionarioDto;
	}
	
}
