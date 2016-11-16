<?php
include_once __DIR__.'/../conf/connect.php';
include_once __DIR__.'/../model/UsuarioSistema.class.php';

class UsuarioSistemaDAO{

	

	function alterarSenha($id, $senha){
		if(!$senha)
			return;
		$senha = md5(mysql_escape_string($senha));
		try{
			$sql = 'UPDATE Usuario SET senha=:senha WHERE id=:id';
			$conn=getConexao();
			$stmt = $conn->prepare($sql);
			$stmt->execute(array(
				':senha'=>$senha,
				':id'=>$id
				));
		}catch(PDOException $e){
			echo 'Erro: '.$e->getMessage();
		}
	}

	function salvar($usuario){
		if($usuario->login=='admin')
			return;
		$usuario->senha = md5(mysql_escape_string($usuario->senha));
		try{
			$sql='INSERT INTO Usuario (login, senha, data_cadastro,permissao) 
						 VALUES (:login,:senha,:data_cadastro,:permissao)';
			$conn = getConexao();
			$stmt = $conn->prepare($sql);
			$stmt->execute(array(
					':login'=>$usuario->login,
					':senha'=>$usuario->senha,
					':data_cadastro'=>$usuario->data_cadastro,
					':permissao'=>$usuario->permissao
				));
			return $conn->lastInsertId();
		}catch(SQLException $e){
			echo 'Erro: '.$e->getMessage();
		}
	}
	function editar($usuario){
		if($usuario->login=='admin')
			$usuario->permissao=1; //não importa oq o usuario colocar o admin sempre vai ter permissao total
		try{
		$sql='UPDATE Usuario SET login=:login, senha=:senha, permissao=:permissao, data_cadastro=:data WHERE id=:id';
		$conn = getConexao();
		$stmt = $conn->prepare($sql);
		$stmt->execute(array(
				':login'=>$usuario->login,
				':senha'=>$usuario->senha,
				':data'=>date("Y-m-d H:i:s"),
				':permissao'=>$usuario->permissao,
				':id'=>$usuario->id
			));
		}catch(SQLException $e){
			echo 'Erro: '.$e->getMessage();	
		}
	}
	function deletar($id){
		try{
			if(!is_numeric($id))
				return;
			$sql="DELETE FROM Usuario WHERE id=$id AND login!='admin'";
			$conn = getConexao();
			$stmt = $conn->prepare($sql);
			$stmt->execute();
		}catch(PDOException $e){
			echo 'Erro: '.$e->getMessage();
		}
	}
	function buscar($id){
		if(!is_numeric($id))
			return;
		$sql="SELECT id,login, permissao, data_ultimo_acesso FROM Usuario WHERE id=$id LIMIT 1";
		$conn = getConexao();
		$result = $conn->query($sql);
		$row = $result->fetch(PDO::FETCH_OBJ);
		$usuario = new UsuarioSistema();
		$usuario->mapear($row);
		return $usuario;
	}
	function login($login, $senha){
		if(function_exists('mysql_escape_string')){
			$login =  mysql_escape_string($login);
			$senha = md5(mysql_escape_string($senha));
		}else{
			$senha = md5($senha);
		}
		/** EXECUTA CONSULTA */
		$sql = "SELECT id, login, permissao FROM Usuario WHERE login='$login' AND senha='$senha' LIMIT 1";
		$conn=getConexao();
		$result = $conn->query($sql);
		$row = $result->fetch(PDO::FETCH_OBJ);
		$usr = new UsuarioSistema();
		$usr->mapear($row);
		//atualiza data de ultimo login
		$this->alterarDataUltimoLogin($usr->id);
		return $usr;
	}
	function alterarDataUltimoLogin($id){
		$data_atual= date("Y-m-d H:i:s");
		try{
			$sql = "UPDATE Usuario SET data_ultimo_acesso='$data_atual' WHERE id=$id";
			$conn=getConexao();
			$stmt = $conn->prepare($sql);
			$stmt->execute();
		}catch(PDOException $e){
			echo 'Erro: '.$e->getMessage();
		}
	}
	function listar(){
		$sql="SELECT * FROM Usuario";
		$conn = getConexao();
		$result = $conn->query($sql);
		if($result)
		while($row = $result->fetch(PDO::FETCH_OBJ)){
			$usuario = new UsuarioSistema();
			$usuario->mapear($row);
			$usuarios[]=$usuario;
		}
		return $usuarios;
	}
}
?>