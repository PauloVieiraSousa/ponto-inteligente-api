package com.pontointeligente.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.enums.PerfilEnum;
import com.pontointeligente.api.utils.PasswordUtils;

@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {
	
	
	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	@Autowired
	EmpresaRepository empresaRepository;
	
	public static final String CNPJ = "51463645000100";
	public static final String EMAIL = "teste@teste.com";
	public static final String CPF = "34164598787";

	
	@BeforeEach
	public void setUp() throws Exception {
		Empresa empresa = this.empresaRepository.save(this.obterDadosDaEmpresa());
		this.funcionarioRepository.save(this.obterDadosFuncionario(empresa));
	}
	
	
	@AfterEach
	public void tearDown() {
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, funcionario.getEmail());
	}
	
	@Test
	public void testBuscaFuncionarioPorCPF() {
		Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);
		assertEquals(CPF, funcionario.getCpf());
	}

	
	@Test
	public void testBuscaFuncionarioPorCpfOuEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, "email@invalido.com.br");
		assertNotNull(funcionario);
	}
	
	

	
	
	public Funcionario obterDadosFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf(CPF);
		funcionario.setNome("Teste nome");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setEmail(EMAIL);
		funcionario.setEmpresa(empresa);
		return funcionario;
	}
	
	
	public Empresa obterDadosDaEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa Teste Mock");
		empresa.setCnpj(CNPJ);
		return empresa;
	}
	

}
