<?php
include __DIR__.'/connect.php';

/** CRIA UMA SESSÃO */
if(!isset($_SESSION)) session_start();


/**VERIFICA SE HOUVE POST E SE O USUARIO OU A SENHA É(SÃO) VAZIOS*/
if(!empty($_POST) AND (empty($_POST['usuario']) OR empty($_POST['senha']))){
	session_destroy();
	header("Location: ../views/login.php");
	exit;
}

/** ABRE CONEXÃO COM BD */
$conn=getConexao();


/** TRATA ENTRADA DE DADOS */
$usuario =  mysql_escape_string($_POST['usuario']);
$senha = md5(mysql_escape_string($_POST['senha']));

/** EXECUTA CONSULTA */
$sql = "SELECT id, login FROM Usuario WHERE login='$usuario' AND senha='$senha' LIMIT 1";
$result = $conn->query($sql);

/** TESTA SE HÁ RESULTADO NA QUERY */
$row =$result->fetch(PDO::FETCH_ASSOC);


if(!$row){
	session_destroy();
	header("Location: ../views/login.php");
	exit;
}


/** SALVA USUARIO NA SESSÃO */
$_SESSION['usuarioID'] = $row['id'];
$_SESSION['usuarioNome']=$row['login'];

/** REDIRECIONA USUARIO PARA PAGINA RESTRITA */
header("Location: ../views/cadastrar/index.php");
exit;

?>