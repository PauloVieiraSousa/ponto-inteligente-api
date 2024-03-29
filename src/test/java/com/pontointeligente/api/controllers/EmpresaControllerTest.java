package com.pontointeligente.api.controllers;


import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.equalTo;

import com.jayway.jsonpath.JsonPath;
import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.services.EmpresaService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmpresaControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private EmpresaService empresaService;

	private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/empresas/cnpj/";
	private static final String CNPJ = "89377232000177";
	private static final Long ID = Long.valueOf(1);
	private static final String RAZAO_SOCIAL = "Empresa XYZ";
	
	@Test
	@WithMockUser
	private void testBuscarEmpresaCnpjInvalido() throws Exception {
		BDDMockito.given(this.empresaService.buscaPorCnpj(Mockito.anyString())).willReturn(Optional.empty());
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.errors").value("Empresa não encontrada para o CNPJ " + CNPJ));
	}
	
	
	@Test
	@WithMockUser
	private void testBuscarEmpresaCnpjValido() throws Exception {
		
		BDDMockito.given(this.empresaService.buscaPorCnpj(Mockito.anyString()))
		.willReturn(this.obterDadosDaEmpresa());
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.id").value(ID))
		.andExpect(jsonPath("$.data.razaoSocial", equalTo(RAZAO_SOCIAL)))
		.andExpect(jsonPath("$.data.cnpj", equalTo(CNPJ)))
		.andExpect(jsonPath("$.data.errors").isEmpty());
		
	}
	
	
	private Optional<Empresa> obterDadosDaEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setId(ID);
		empresa.setCnpj(CNPJ);
		empresa.setRazaoSocial(RAZAO_SOCIAL);
		return Optional.of(empresa);
	}
	
	
	
	
	

}
