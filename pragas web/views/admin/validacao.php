<?php
include_once $_SERVER['DOCUMENT_ROOT'].'/dao/UsuarioSistemaDAO.class.php';

/** CRIA UMA SESSÃO */
if(!isset($_SESSION)) session_start();


/**VERIFICA SE HOUVE POST E SE O USUARIO OU A SENHA É(SÃO) VAZIOS*/
if(!empty($_POST) AND (empty($_POST['usuario']) OR empty($_POST['senha']))){
	session_destroy();
	header("Location: login.php");
	exit;
}

/** TRATA ENTRADA DE DADOS */
$dao = new UsuarioSistemaDAO();
$usuario = $dao->login($_POST['usuario'], $_POST['senha']);

//verifica se existe login
if(!$usuario->id){
	session_destroy();
	header("Location: ../../../views/admin/login.php");
	exit;
}

/** SALVA USUARIO NA SESSÃO */
$_SESSION['usuarioID'] = $usuario->id;
$_SESSION['usuarioNome']=$usuario->login;
$_SESSION['usuarioPermissao']=$usuario->permissao;


/** REDIRECIONA USUARIO PARA PAGINA RESTRITA */
header("Location: ../../../views/admin/admin.php");
exit;

?>
