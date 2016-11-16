<?php

include_once __DIR__.'/../conf/connect.php';
include_once __DIR__.'/../model/Autor.class.php';
include_once __DIR__.'/../resource/util/util.php';
include_once __DIR__.'/../views/admin/salvaFoto.php';
define('DIR_FOTO', __DIR__.'/../resource/img/thumbnail/autor/');

class AutorDAO{

	function editar($autor){
		try{
		$conn = getConexao();
		$sql = 'UPDATE Autor
				SET
					nome=:nome,
					email=:email,
					telefone=:telefone,
					telefone2=:telefone2,
					descricao=:descricao
				WHERE id=:id';
		$stmt = $conn->prepare($sql);

		$stmt->execute(array(
				':nome'		=>$autor->nome,
				':email'	=>$autor->email,
				':telefone'	=>$autor->telefone,
				':telefone2'=>$autor->telefone2,
				':descricao'=>$autor->descricao,
				':id'=>$autor->id
			));
		}catch(PDOException $e){
			echo '<i>Não foi possível editar dados do autor, Tente novamente!</i>';
		}
	}
	function salvar($autor){
		try{
			$conn = getConexao();
			$sql = "INSERT INTO Autor (nome, email, telefone, telefone2, descricao)
					       VALUES (:nome, :email, :telefone, :telefone2, :descricao)";

			$stmt = $conn->prepare($sql);
			$stmt->execute(array(
					':nome'		=>$autor->nome,
					':email'	=>$autor->email,
					':telefone'	=>$autor->telefone,
					':telefone2'=>$autor->telefone2,
					':descricao'=>$autor->descricao
				));
			//retorna id da inserção
			return $conn->lastInsertId();
		}catch(PDOException $e){
			echo '<i>Não foi possível salvar autor, Tente novamente!</i>';
			
		}
	}
	function deletar($autor_id){
		try{
			if(!is_numeric($autor_id))
				return;
			$conn=getConexao();
			$sql = 'DELETE FROM Autor WHERE id=:autor_id';
			$stmt = $conn->prepare($sql);
			$stmt->execute(array(':autor_id'=>$autor_id));
		}catch(PDOException $e){
			if($e->getCode()==23000){
				echo '<i>Não foi possível deletar autor, ele está relacionado há uma ou mais Pragas.</i>';
			}
		}
	}
	//busca todos os autores
	//(WEB SERVICE)
	function listarAutoresParaWebService(){
		$conn = getConexao(); 
		$sql='SELECT * FROM Autor';
		$result = $conn->query($sql);		
		while($row = $result->fetch(PDO::FETCH_ASSOC)){
			$autor = new Autor();
			$autor->mapear($row);
			//recupera foto
			$foto = arquivoParaBinario(DIR_FOTO.$autor->id.'.jpeg');
			$autor->strFoto = base64_encode($foto);
			$array[]=$autor;
		}
		return $array;
	}

	//busca infomações do autor de acordo com a praga desejada, é usado para mostrar breve informação do autor com foto
	function buscarAutorPorPraga($id_praga){
		$conn = getConexao(); 
		$sql="SELECT a.id as id, a.nome as nome, a.foto as foto 
			  FROM Praga p, Autor a 
			  WHERE p.id=$id_praga AND p.autor=a.id LIMIT 1";
	  	$result = $conn->query($sql);
		$autor = $result->fetch(PDO::FETCH_OBJ);
		
		return $autor;
	}
	//busca autor
	function buscar_autor($id){
		if(!is_numeric($id))
			return;
		$conn = getConexao();
		$sql = "SELECT * FROM Autor WHERE id=$id";
		$result = $conn->query($sql);
		$row = $result->fetch(PDO::FETCH_OBJ);
		$a = new Autor();
		$a->mapear($row);
		return $a;
	}
	//lista informações basicas do autor, essas informações podem ser usadas em um selectbox
	function listar_info_basico(){
		$conn = getConexao();
		$sql = "SELECT id, nome FROM Autor";
		$result = $conn->query($sql);
		while($row = $result->fetch(PDO::FETCH_OBJ)){
			$autor = new Autor();
			$autor->mapear($row);
			$autores[] = $autor;
		}
		return $autores;
	}
}
?>
