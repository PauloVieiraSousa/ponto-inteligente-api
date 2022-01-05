package com.pontointeligente.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.pontointeligente.api.entities.Empresa;

@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {
	
	@Autowired
	private EmpresaRepository  empresaRepository;

	public static final String CNPJ = "51463645000100";
	
	@BeforeEach
	public void setUp() throws Exception{
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa Mock");
		empresa.setCnpj(CNPJ);
		empresaRepository.save(empresa);
	}
	
	@AfterEach
	public void tearDown() {
		this.empresaRepository.deleteAll();
	}

	
	@Test
	public void testBuscaEmpresaPorCNPJ () {
		Empresa empresa = this.empresaRepository.findByCnpj(CNPJ);
		assertEquals(CNPJ, empresa.getCnpj());
	
	}
}
