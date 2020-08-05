package br.com.maa.escolamaa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.maa.escolamaa.models.Aluno;
import br.com.maa.escolamaa.models.Habilidade;
import br.com.maa.escolamaa.services.AlunoService;

@Controller
public class HabilidadeController {

	@Autowired
	private AlunoService servico;

	@GetMapping("habilidade/cadastrar/{id}")
	public String cadastrar(@PathVariable String id, Model model) {

		Aluno aluno = servico.getById(id);
		model.addAttribute("aluno", aluno);

		model.addAttribute("habilidade", new Habilidade());

		return "habilidade/cadastrar";

	}

	@PostMapping("habilidade/salvar/{id}")
	public String salvar(@PathVariable String id, @ModelAttribute Habilidade habilidade) {

		Aluno aluno = servico.getById(id);

		aluno.adiciona(habilidade);

		servico.salvar(aluno);

		return "redirect:/aluno/listar";

	}
}
