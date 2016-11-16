<?php
include_once __DIR__.'/../conf/connect.php';
include_once __DIR__.'/../model/Praga.class.php';
include_once __DIR__.'/../model/Autor.class.php';
include_once __DIR__.'/../model/PragaFoto.class.php';
include_once __DIR__.'/../model/Categoria.class.php';
include_once __DIR__.'/../resource/util/util.php';


class PragaDAO{
	
	//conta todas as pragas
	function getTotal(){
		$conn= getConexao();
		$sql='SELECT count(id) as total FROM Praga';
		$result=$conn->query($sql);
		$row = $result->fetch(PDO::FETCH_ASSOC);
		return $row['total'];
	}

	function listaIdsPragasExistentes(){
		$conn= getConexao();
		$sql='SELECT id FROM Praga';
		$query=$conn->query($sql);
		$result = array();
		if($query){
			while($row = $query->fetch(PDO::FETCH_ASSOC)){
				$result[] = $row['id'];
			}
		}
		return $result;
	}

	//desenvolvido para webservice buscar apenas uma praga por vez
	//(PARA O WEBSERVICE)
	function listarPragasComLimit(){
		$conn= getConexao();
		$sql='SELECT * FROM Praga LIMIT 1' ;
		//return new Praga($conn->query($sql));
		$result = $conn->query($sql);
		//Transforma resultados em objetos Praga
		$pragas = $this->mapearResultado($result);
		return $pragas;
	}

	function editar($praga){
		$praga->versaoPublicacao=$praga->versaoPublicacao+1;
		$sql = 'UPDATE Praga 
				SET 
					nome = :nome,
					nome_cientifico = :nome_cientifico,
					autor = :autor,
					descricao = :descricao,
					biologia = :biologia,
					comportamento = :comportamento,
					danos = :danos,
					localizacao = :localizacao,
					controle = :controle,
					bibliografia= :bibliografia,
					versao_publicacao= :versao_publicacao,
					data_alteracao= :data_alteracao
				WHERE id = :id ';
		try{
			$conn = getConexao();
			$stmt = $conn->prepare($sql);
			$stmt->execute(array(
				':nome'=>$praga->nome,
				':nome_cientifico'=>$praga->nomeCientifico,
				':autor'=>$praga->autor->id,
				':descricao'=>$praga->descricao,
				':biologia'=>$praga->biologia,
				':comportamento'=>$praga->comportamento,
				':danos'=>$praga->danos,
				':localizacao'=>$praga->localizacao,
				':controle'=>$praga->controle,
				':bibliografia'=>$praga->bibliografia,
				':versao_publicacao'=>$praga->versaoPublicacao,
				':data_alteracao'=>date("Y-m-d H:i:s"),
				':id'=>(int)$praga->id
			));
		}catch(PDOException $e){
			echo 'Error: ' . $e->getMessage();	
		}
	}
	
	//salva no banco de dados
	function salvar($praga){
		if(!($praga instanceof Praga) || $praga==null)
			return;
		try{
			$conn = getConexao();

			$sql="INSERT INTO Praga 
			(nome, nome_cientifico, versao_publicacao,autor,
				data_criacao,descricao,biologia,comportamento,danos,localizacao,controle,bibliografia)
			VALUES
			(:nome, :nome_cientifico, :versao_publicacao,:autor,
			:data_criacao,:descricao,:biologia,:comportamento,:danos,:localizacao,:controle,:bibliografia)";
			
			$stmt = $conn->prepare($sql);
			$stmt->execute(array(
				':nome'=>$praga->nome,
				':nome_cientifico'=>$praga->nomeCientifico,
				':versao_publicacao'=>1,
				':autor'=>$praga->autor->id,
				':data_criacao'=>date("Y-m-d H:i:s"),
				':descricao'=>$praga->descricao,
				':biologia'=>$praga->biologia,
				':comportamento'=>$praga->comportamento,
				':danos'=>$praga->danos,
				':localizacao'=>$praga->localizacao,
				':controle'=>$praga->controle,
				':bibliografia'=>$praga->bibliografia,
			));
		}catch(PDOException $e){
				echo 'Error: ' . $e->getMessage();
		}
		//retorna id
		return $conn->lastInsertId();
	}

	/**
	* Método responsável por contar as pragas que o cliente não possui, o parametro recebido é um map 
	* que contem id e versao das pragas que o cliente possui,
	**/
	function getCountPragas($map){
			$conn= getConexao();//abre conexao com banco de dados

			if(count($map)==0)//se o array for vazio entao nao precisa filtrar, basta devoltar contagem total
				return $this->getTotal();

			$sql = "SELECT count(id) as total
	            FROM Praga WHERE ("; //sql base
		    $sql2="";//sql rodape

		    $cont=0;//contador apenas para identificar inicio do loop;
		    foreach ($map as $key => $value) {
				if($cont==0){//condição válida apenas na primeira rodada do loop, ajustando prefixos do sql, OR e parenteses.
					$sql.= "(id=$key AND versao_publicacao>$value) ";//verifica versao da praga se esta desatualizada
					$sql2.=" OR ((id!=$key) ";	//separa sql2 do sql1 com OR
				}else{
					$sql.= " OR (id=$key AND versao_publicacao>$value) ";//
					$sql2.=" AND (id!=$key) ";	//concatena todos os ids com AND, se todos ids forem diferentes então,
												//significa que cliente ainda não baixou essa praga.
				}
				$cont++;
			}
		    $sql.=")";//finaliza sql1
			$sql2.=") LIMIT 1";//finaliza sql2
			$sql.=$sql2;//concatena os dois sql formando um apenas

			$result=$conn->query($sql);
			$row = $result->fetch(PDO::FETCH_ASSOC);
			return $row['total'];

	}
	/**
	 * Método responsável por receber um array com todos os ids e versões de
	 * publicação das pragas que cliente ja possui, e retorna novas pragas que
	 * cliente ainda não tem, ou pragas ATUALIZADAS DE ACORDO com versão de publicação diferentes da q
	 * o cliente ja possui.
	 * (PARA O WEBSERVICE)
	 **/
	function filtrarPragas($map){
		$conn= getConexao();//abre conexao com banco de dados

		if(count($map)==0)//se o array for vazio entao nao precisa filtrar, basta listar todas
		return $this->listarPragasComLimit();

		$sql = "SELECT *
	            FROM Praga WHERE ("; //sql base
	    $sql2="";//sql rodape

	    $cont=0;//contador apenas para identificar inicio do loop;
	    foreach ($map as $key => $value) {
			if($cont==0){//condição válida apenas na primeira rodada do loop, ajustando prefixos do sql, OR e parenteses.
				$sql.= "(id=$key AND versao_publicacao>$value) ";//verifica versao da praga se esta desatualizada
				$sql2.=" OR ((id!=$key) ";	//separa sql2 do sql1 com OR
			}else{
				$sql.= " OR (id=$key AND versao_publicacao>$value) ";//
				$sql2.=" AND (id!=$key) ";	//concatena todos os ids com AND, se todos ids forem diferentes então,
											//significa que cliente ainda não baixou essa praga.
			}
			$cont++;
		}
	    $sql.=")";//finaliza sql1
		$sql2.=") LIMIT 1";//finaliza sql2
		$sql.=$sql2;//concatena os dois sql formando um apenas

		$result = $conn->query($sql);
		//Transforma resultados em objetos Praga
		$pragas = $this->mapearResultado($result);
		//retornar array com todas as pragas que cliente ainda não tem, ou pragas atualizadas.
		return $pragas;
	}

	//FUNÇÃO QUE MAPEIA UM ARRAY DE RESULTADOS DO BD PARA UM ARRAY DE OBJETOS DO TIPO PRAGA para webservice
	function mapearResultado($result){
		$array = array();
		while($row = $result->fetch(PDO::FETCH_ASSOC)){
			//instancia nova praga
			$praga = new Praga();
			//mapeia resultado em objeto
			$praga->mapear($row);
			//seta nulo as datas, pois as datas do php é diferente do java(android), preciso de uma frma de convertelas
			$praga->dataCriacao=null;
			$praga->dataAlteracao=null;
			//busca todas as categorias da praga, não precisa é só da soja.
			//$praga->categorias = $this->listarCategorias($praga->id);
			//busca fotos da praga... 
			$praga->pragaFotos = $this->listarFotos($praga->id);
			//cria thumbnail
			//$praga->strThumbnail = base64_encode($this->getThumbnail($praga->id));
			//adiciona praga ao array de pragas
			$array[]=$praga;
		}
		return $array;
	}
	function getThumbnail($praga_id){
		//recupera endereço de uma foto
		$sql = "SELECT id FROM Praga_Foto WHERE praga_id = $praga_id LIMIT 1";
		$conn=getConexao();
		$result = $conn->query($sql);
		$foto = $result->fetch(PDO::FETCH_OBJ);
		//caminho do tumbnail
		$nome = $praga_id.'-'.$foto->id.'.jpg';
		$src = $_SERVER['DOCUMENT_ROOT']."/resource/img/thumbnail/".$nome;
		//transfoma foto em binario
		return arquivoParaBinario($src);
	}

	//BUSCA TODAS AS FOTOS DE UMA PRAGA
	function buscar_fotos($id_praga){
		$conn=getConexao();
		$id_praga = tratar_entrada($id_praga);

		$result = $conn->query(
			"SELECT f.descricao as descricao, f.id as id, f.fotografo as fotografo
			FROM Praga p, Praga_Foto f 
			WHERE p.id=f.praga_id AND f.praga_id=$id_praga"
			);

		while($row = $result->fetch(PDO::FETCH_OBJ)){
			$fotos[]=$row;
		}
		return $fotos;
	}



	/*FUNÇÃO QUE BUSCA TODAS AS FOTOS DE UMA PRAGA*/
	function listarFotos($idPraga){
		//cria conexao
		$conn= getConexao();
		//sql de consulta
		$sql ="SELECT f.id as id, f.descricao as descricao, f.fotografo as fotografo FROM Praga_Foto f, Praga p WHERE p.id = f.praga_id AND p.id=$idPraga";
		//executa query
		$result = $conn->query($sql);
		//array com todas as fotos
		$array_fotos = array();

		//verificia resultado do sql
		if($result){
			//percorre td o array de resultado para converter binarios em base64
			while($row = $result->fetch(PDO::FETCH_OBJ)){
				
				//instancia objeto foto
				$foto = new PragaFoto();
				//seta id da foto
				$foto->id = $row->id;
				$foto->id_praga = $idPraga;
				//pega foto e codifica binario para base64.. NÃO FAZ MAIS CONVERAO, DEVIDO AO ESTOURO DE MEMÓRIA NO ANDROID, AS FOTOS SÃO BAIXADAS SEPARADAMENTE
				/*$nome = $idPraga.'-'.$row->id.'.jpg';
				$src = $_SERVER['DOCUMENT_ROOT'].'/resource/img/fotos/'.$nome;
				$foto->fotoBase64 = base64_encode(arquivoParaBinario($src));*/
				$foto->descricao = $row->descricao;
				$foto->fotografo = $row->fotografo;
				//Adiciona ao array de fotos
				$array_fotos[]=$foto;
			}
		}
		return $array_fotos;
	}

	//FUNÇÃO LISTA INFORMAÇÕES BÁSICAS
	function listar_basico_pragas(){
		$conn = getConexao();
		$sql = 'SELECT p.id as id, p.nome as nome, p.nome_cientifico as nome_cientifico, f.id as descricao_1foto
		FROM Praga p
		LEFT JOIN Praga_Foto f
		ON p.id=f.praga_id
		GROUP BY p.id';
		$result = $conn->query($sql);
		while($row = $result->fetch(PDO::FETCH_OBJ)){
			$p = new Praga();
			$p->mapear($row);
			$pragas[]=$p;
		}
		return $pragas;
	}
	//FILTRA PRAGAS DE ACORDO COM NOME
	function filtrar_pragas($strPesquisa){
		$conn = getConexao();
		$strPesquisa = tratar_entrada($strPesquisa);
		$strPesquisa.='%';
		$sql = "SELECT p.id as id, p.nome as nome, p.descricao as descricao, f.descricao as descricao_1foto
				FROM Praga p
				LEFT JOIN Praga_Foto f
				ON p.id=f.praga_id
				WHERE p.nome LIKE '$strPesquisa'
				GROUP BY p.nome";
		$result = $conn->query($sql);
		while($row = $result->fetch(PDO::FETCH_OBJ)){
			$p = new Praga();
			$p->mapear($row);
			$pragas[]=$p;
		}
		return $pragas;
	}

	//BUSCA PRAGA POR ID
	function buscar_praga($id){
		$conn=getConexao();
		$id = tratar_entrada($id);
		$result = $conn->query("SELECT * FROM Praga WHERE id = $id LIMIT 1");
		$row = $result->fetch(PDO::FETCH_ASSOC);
		$p = new Praga();
		$p->mapear($row);
		return $p;
	}
	function deletar($id){
		if(!is_numeric($id))
			return;


		$this->remover_foto_por_praga($id);
		$conn=getConexao();
		$sql = "DELETE FROM Praga WHERE id=:id";
		$stmt=$conn->prepare($sql);
		$stmt->bindParam(':id', $id, PDO::PARAM_INT);
		$stmt->execute();
	}

	//salva foto, id da praga e nome no campo descricao
	function salvarFoto($foto){
		try{
			$conn = getConexao();
			$sql = "INSERT INTO Praga_Foto (descricao, praga_id,fotografo) VALUES (:descricao, :praga_id, :fotografo)";
			$stmt=$conn->prepare($sql);
			$stmt->execute(array(
				':descricao'=>$foto->descricao,
				':praga_id'=>$foto->praga->id,
				':fotografo'=>$foto->fotografo
			));
			return $conn->lastInsertId();
		}catch(PDOException $e){
			echo 'Error: '.$e->getMessage();
		}
	}
	//metodo responsavel por remover fotos de uma praga
	function remover_foto_por_praga($id_praga){
		//recupera dados da foto para remover primeiro arqivo fisico
		$sql = "SELECT id FROM Praga_Foto WHERE praga_id = $id_praga";
		$conn = getConexao();
		$result = $conn->query($sql);
		//deleta as fotos do diretorios de fotos
		while($row = $result->fetch(PDO::FETCH_ASSOC)){
			$nome=$id_praga.'-'.$row['id'].'.jpg';
			$dir = __DIR__.'/../resource/img/fotos/'.$nome;
			$dir_tumb=__DIR__.'/../resource/img/thumbnail/'.$nome;
			if(file_exists($dir))
				unlink($dir);
			if(file_exists($dir_tumb))
				unlink($dir_tumb);
		}

		//deleta todos os dados das fotos salvo no banco de dados
		$sql = "DELETE FROM Praga_Foto WHERE praga_id = $id_praga";
		$conn = getConexao();
		$stmt = $conn->prepare($sql);
		$stmt->execute();

	}

	//metodo responsavel por remover fotos conforme array de IDS passado
	function remover_fotos($ids){
		$condicao = " id = $ids[0] ";
		foreach ($ids as $id) {
			$condicao .= " OR id = $id";
		}
		//PRIMEIRO PASSO: RECUPERAR OS NOMES PARA REMOVER ARQUIVO FISICO
		$conn = getConexao();
		$sql = "SELECT id, praga_id FROM Praga_Foto WHERE $condicao";
		$result =  $conn->query($sql);
		//REMOVE ARQUIVOS FISICOS
		while($row = $result->fetch(PDO::FETCH_ASSOC)){
			$nome=$row['praga_id'].'-'.$row['id'].'.jpg';
			$dir = __DIR__.'/../resource/img/fotos/'.$nome;
			$dir_tumb=__DIR__.'/../resource/img/thumbnail/'.$nome;
			if(file_exists($dir))
				unlink($dir);
			if(file_exists($dir_tumb))
				unlink($dir_tumb);
		}
		//SEGUNDO PASSO: REMOVER INFORMACOES DO ARQUIVO NO BANCO DE DADOS
		$sql = 'DELETE FROM Praga_Foto WHERE '.$condicao;
		$conn = getConexao();
		$stmt = $conn->prepare($sql);
		$stmt->execute();
	}
	
}
?>