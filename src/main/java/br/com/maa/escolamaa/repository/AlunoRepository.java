package br.com.maa.escolamaa.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import br.com.maa.escolamaa.codecs.AlunoCodec;
import br.com.maa.escolamaa.models.Aluno;

@Repository
public class AlunoRepository {

	private MongoClient cliente;
	private MongoDatabase bancoDeDados;

	private void fecharConexao() {
		cliente.close();
	}

	private void criarConexao() {

		MongoClientOptions options = criarCodec();

		cliente = new MongoClient("localhost:27017", options);
		bancoDeDados = cliente.getDatabase("test");
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

	private List<Aluno> getAlunoList(MongoCursor<Aluno> resultado) {
		List<Aluno> alunosEncontrados = new ArrayList<>();

		while (resultado.hasNext()) {
			Aluno aluno = resultado.next();
			alunosEncontrados.add(aluno);
		}
		return alunosEncontrados;
	}

	public void salvar(Aluno aluno) {

		criarConexao();

		MongoCollection<Aluno> alunos = bancoDeDados.getCollection("alunos", Aluno.class);

		if (aluno.getId() == null) {
			alunos.insertOne(aluno);
		} else {

			alunos.updateOne(Filters.eq("_id", aluno.getId()), new Document("$set", aluno));
		}

		fecharConexao();

	}

	public List<Aluno> getAll() {

		criarConexao();
		MongoCollection<Aluno> alunos = this.bancoDeDados.getCollection("alunos", Aluno.class);

		MongoCursor<Aluno> resultado = alunos.find().iterator();

		List<Aluno> alunosEncontrados = getAlunoList(resultado);

		fecharConexao();
		return alunosEncontrados;
	}

	public Aluno getById(String id) {
		criarConexao();
		MongoCollection<Aluno> alunos = this.bancoDeDados.getCollection("alunos", Aluno.class);

		Aluno aluno = alunos.find(Filters.eq("_id", new ObjectId(id))).first();
		fecharConexao();

		return aluno;
	}

	public List<Aluno> getAllByNota(String classificacao, double notaCorte) {
		criarConexao();
		MongoCollection<Aluno> alunos = this.bancoDeDados.getCollection("alunos", Aluno.class);

		MongoCursor<Aluno> resultado;
		if (classificacao.equalsIgnoreCase("reprovados")) {
			resultado = alunos.find(Filters.lt("notas", notaCorte)).iterator();
		} else {
			resultado = alunos.find(Filters.gte("notas", notaCorte)).iterator();
		}

		List<Aluno> alunosEncontratos = getAlunoList(resultado);

		fecharConexao();

		return alunosEncontratos;
	}

	public List<Aluno> getByName(String nome) {
		criarConexao();
		MongoCollection<Aluno> alunos = this.bancoDeDados.getCollection("alunos", Aluno.class);

		MongoCursor<Aluno> resultado;

		resultado = alunos.find(Filters.eq("nome", nome)).iterator();

		List<Aluno> alunosEncontratos = getAlunoList(resultado);

		fecharConexao();

		return alunosEncontratos;
	}

	public List<Aluno> getAlunosProximos(Aluno aluno) {

		criarConexao();
		MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("alunos", Aluno.class);
		alunoCollection.createIndex(Indexes.geo2dsphere("contato"));

		List<Double> coordinates = aluno.getContato().getCoordinates();
		Point pontoReferencia = new Point(new Position(coordinates.get(0), coordinates.get(1)));

		MongoCursor<Aluno> resultados = alunoCollection
				.find(Filters.nearSphere("contato", pontoReferencia, 2000.0, 0.0)).limit(2).skip(1).iterator();
		List<Aluno> alunos = getAlunoList(resultados);

		fecharConexao();
		return alunos;
	}

}
