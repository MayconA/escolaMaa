package br.com.maa.escolamaa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.maa.escolamaa.models.Aluno;
import br.com.maa.escolamaa.services.AlunoService;

@Controller
public class GeolocalizacaoController {

	@Autowired
	private AlunoService servico;

	@GetMapping("/geolocalizacao/iniciarpesquisa")
	public String inicializarPesquisa(Model model) {
		List<Aluno> alunos = servico.obterTodosAlunos();
		model.addAttribute("alunos", alunos);

		return "geolocalizacao/pesquisar";
	}

	@GetMapping("/geolocalizacao/pesquisar")
	public String pesquisar(@RequestParam("alunoId") String alunoId, Model model) {
		Aluno aluno = servico.getById(alunoId);

		List<Aluno> alunosProximos = servico.getAlunosProximos(aluno);

		model.addAttribute("alunosProximos", alunosProximos);
		return "geolocalizacao/pesquisar";
	}

}
