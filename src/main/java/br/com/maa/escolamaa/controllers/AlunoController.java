package br.com.maa.escolamaa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.maa.escolamaa.models.Aluno;
import br.com.maa.escolamaa.services.AlunoService;

@Controller
public class AlunoController {
	
	@Autowired
	private AlunoService servico;

	@GetMapping("/aluno/cadastrar")
	public String cadastrar(Model model) {

		model.addAttribute("aluno", new Aluno());
		
		return "aluno/cadastrar";
	}
	
	@PostMapping("/aluno/salvar")
	public String salvar(@ModelAttribute Aluno aluno) {
		
		servico.salvar(aluno);
		
		return "redirect:/";		
		
	}
	
	@GetMapping("/aluno/listar")
	public String Listar(Model model) {
		
		List<Aluno> alunos = servico.obterTodosAlunos();
		model.addAttribute("alunos", alunos);
		return "aluno/listar";
	}
}
