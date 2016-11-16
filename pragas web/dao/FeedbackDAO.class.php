<?php
include_once $_SERVER['DOCUMENT_ROOT'].'/conf/connect.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/model/Feedback.class.php';

class FeedbackDAO{

	function deletar($id){
		try{
		if(!is_numeric($id))
			return;
		$conn=getConexao();
		$sql = 'DELETE FROM Feedback WHERE id=:id';
		$stmt = $conn->prepare($sql);
		$stmt->execute(array(':id'=>$id));
		}catch(PDOException $e){
			echo '   Erro: '.$e->getMessage();	
		}
	}
	function salvar($fb){
		$fb->antSQLInjection();
		try{
			//cria conexao
			$conn = getConexao();
			
			$sql = "INSERT INTO Feedback (nome_usuario,email,descricao, data) 
					VALUES (:nome_usuario, :email, :descricao,:data)";
			//prepara sql
			$stmt = $conn->prepare($sql);
			//INSERE DADOS
			$stmt->execute(array(
				':nome_usuario'=>$fb->nomeUsuario,
				':email'=>$fb->email,
				':data'=>$fb->data,
				':descricao'=>$fb->descricao
				));
		}catch(PDOException $e) {
		  echo 'Error: ' . $e->getMessage();
		  return false;
		}
		return true;
	}

	function listar(){
		$sql = 'SELECT * FROM Feedback ORDER BY data DESC';
		$conn=getConexao();
		$result = $conn->query($sql);
		while($row = $result->fetch(PDO::FETCH_OBJ)){
			$f = new Feedback();
			$f->mapear($row);
			$f->nomeUsuario = $row->nome_usuario;
			$feedbacks[]=$f;
		}
		return $feedbacks;
	}

}

?>