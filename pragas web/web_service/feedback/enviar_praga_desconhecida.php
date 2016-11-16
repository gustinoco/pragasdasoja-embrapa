<?php
include_once '../../conf/connect.php';
include_once '../../dao/UsuarioRetornoDAO.class.php';
include_once '../../model/UsuarioRetorno.class.php';

if(empty($_POST) || empty($_POST['json'])){
	die("falha");
}
//DECODIFICA OBJETO FEEDBACK
$obj = json_decode($_POST['json']);
$obj->data = date("Y-m-d H:i:s");

$usr = new UsuarioRetorno();
$usr->mapear($obj);



$dao = new UsuarioRetornoDAO();

if($dao->salvar($usr))
	echo 'sucesso';
else
	echo 'falha';
?>