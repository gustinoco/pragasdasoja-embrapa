<?php 

require_once '../restrito.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/dao/AutorDAO.class.php';
define("DIR_FOTOS", "../../../resource/img/thumbnail/autor/");

$dao = new AutorDAO();

if(isset($_POST) AND (count($_POST)>0)){
	$autor = new Autor();
	$autor->mapear($_POST);

	if($autor->id>0){
		$dao->editar($autor);
		salvarFoto($_FILES,$autor->id,DIR_FOTOS);
	}else{
		$id_autor = $dao->salvar($autor);
		//salvar fotos
		salvarFoto($_FILES,$id_autor,DIR_FOTOS);
	}

}
header('Location: ../consultar/autor.php');
exit;
?>