<?php 
require_once '../restrito.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/dao/UsuarioSistemaDAO.class.php';

$dao = new UsuarioSistemaDAO();

if(isset($_POST) AND (count($_POST)>0)){

	$usuario = new UsuarioSistema();
	$usuario->mapear($_POST);
	
	if($usuario->id>0){
		$dao->editar($usuario);
		//verifica se usuario deseja trocar senha
		if($usuario->senha!=null)
			$dao->alterarSenha($usuario->id, $usuario->senha);
	}else{
		$id_autor = $dao->salvar($usuario);
	}

}
header('Location: ../consultar/usuario.php');
exit;
?>