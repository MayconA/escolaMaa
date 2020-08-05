package br.com.maa.escolamaa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.maa.escolamaa.models.Aluno;
import br.com.maa.escolamaa.models.Nota;
import br.com.maa.escolamaa.services.AlunoService;

@Controller
public class NotaController {

	@Autowired
	private AlunoService servico;

	@GetMapping("nota/cadastrar/{id}")
	public String cadastrar(@PathVariable String id, Model model) {

		Aluno aluno = servico.getById(id);
		model.addAttribute("aluno", aluno);

		model.addAttribute("notas", new Nota());

		return "nota/cadastrar";
	}

	@PostMapping("nota/salvar/{id}")
	public String salvar(@PathVariable String id, @ModelAttribute Nota nota) {
		Aluno aluno = servico.getById(id);

		aluno.adiciona(nota);

		servico.salvar(aluno);

		return "redirect:/aluno/listar";
	}

	@GetMapping("/nota/iniciarpesquisa")
	public String iniciarPesquisa() {
		return "nota/iniciarpesquisa";
	}

	@GetMapping("/nota/pesquisar")
	public String pesquisar(@RequestParam("classificacao") String classificacao,
			@RequestParam("notacorte") String notaCorte, Model model) {

		List<Aluno> alunos = servico.getAllByNota(classificacao, Double.parseDouble(notaCorte));

		model.addAttribute("alunos", alunos);

		return "nota/iniciarpesquisa";
	}

}
