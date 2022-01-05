package com.pontointeligente.api.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pontointeligente.api.entities.Lancamento;
import com.pontointeligente.api.repositories.LancamentoRepository;


@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	
	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
		log.info("Buscar funcionario por id {}", funcionarioId);
		return lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Override
	@Cacheable("lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buscar por id {}", id);
		return lancamentoRepository.findById(id);
	}

	@Override
	@CachePut("lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Persistind lancamento {}", lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo lancamento ID {}", id);
		lancamentoRepository.deleteById(id);
	}

}
