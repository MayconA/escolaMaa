package br.com.maa.escolamaa.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.maa.escolamaa.models.Aluno;
import br.com.maa.escolamaa.repository.AlunoRepository;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository repositorio;

	public void salvar(Aluno aluno) {

		repositorio.salvar(aluno);

	}

	public List<Aluno> obterTodosAlunos() {
		
		return repositorio.getAll();
	}
}
