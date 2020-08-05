package br.com.maa.escolamaa.models;

public class Curso {
	private String nome;
	
	public Curso() {
		
	}

	public Curso(String nomeCurso) {
		nome = nomeCurso;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
