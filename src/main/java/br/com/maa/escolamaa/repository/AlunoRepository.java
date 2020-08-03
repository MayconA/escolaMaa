package br.com.maa.escolamaa.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import br.com.maa.escolamaa.codecs.AlunoCodec;
import br.com.maa.escolamaa.models.Aluno;

@Repository
public class AlunoRepository {

	private MongoClient cliente;
	private MongoDatabase bancoDeDados;

	private void criarConexao() {

		MongoClientOptions options = criarCodec();

		cliente = new MongoClient("localhost:27017", options);
		bancoDeDados = cliente.getDatabase("test");
	}

	public void salvar(Aluno aluno) {

		criarConexao();

		MongoCollection<Aluno> alunos = bancoDeDados.getCollection("alunos", Aluno.class);
		alunos.insertOne(aluno);
		// cliente.close();

	}

	private MongoClientOptions criarCodec() {
		/*
		 * estamos criando um codec na mão. é como se fosse um modelMapper. mas existem
		 * bibliotecas que ja fazem isso
		 * 
		 * aqui estamos só exercitando
		 * 
		 */
		Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
		AlunoCodec alunoCodec = new AlunoCodec(codec);

		CodecRegistry registro = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
				CodecRegistries.fromCodecs(alunoCodec));

		MongoClientOptions options = MongoClientOptions.builder().codecRegistry(registro).build();
		return options;
	}

	public List<Aluno> getAll() {

		criarConexao();
		MongoCollection<Aluno> alunos = this.bancoDeDados.getCollection("alunos", Aluno.class);

		MongoCursor<Aluno> resultado = alunos.find().iterator();

		List<Aluno> alunosEncontrados = new ArrayList<>();

		while (resultado.hasNext()) {
			Aluno aluno = resultado.next();
			alunosEncontrados.add(aluno);
		}

		return alunosEncontrados;
	}

}
