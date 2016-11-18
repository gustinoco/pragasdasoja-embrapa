<?php 
require_once '../restrito.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/dao/ApresentacaoDAO.php';

$dao = new ApresentacaoDAO();

if(isset($_POST) AND (count($_POST)>0)){

	$apresentacao = new Apresentacao();
	$apresentacao->mapear($_POST);	
	$dao->editar($apresentacao);		
}
header('Location: ../cadastrar/apresentacaoapp.php');
exit;
?>