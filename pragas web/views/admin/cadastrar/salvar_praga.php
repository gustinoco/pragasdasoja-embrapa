<?php 

require_once '../restrito.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/dao/PragaDAO.class.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/model/PragaFoto.class.php';

include '../salvaFoto.php';
define("DIR_FOTOS", $_SERVER['DOCUMENT_ROOT']."/resource/img/fotos/");

$daoP = new PragaDAO();

if(isset($_POST) AND (count($_POST)>0)){
	$praga = new Praga();
	$praga->mapear($_POST);

	if($praga->id > 0){
		$daoP->editar($praga);
	}else{
		$praga->id = $daoP->salvar($praga);		
	}

	//salvar fotos 
	//primeiro recupera informações das fotos
	$fotos = array();
	for($i=0;$i<count($_FILES['fotos']['name']);$i++){
		if($files["fotos"]["error"][$i] != 0)
			continue;
		$foto = new PragaFoto();
		$foto->nome = $_FILES['fotos']['name'][$i];
		$foto->descricao = $_POST['descricao_foto'.$i] ;
		$foto->fotografo = $_POST['fotografo_foto'.$i] ;
		$foto->tmp_name = $_FILES['fotos']['tmp_name'][$i];
		$foto->praga = $praga;
		$fotos[] = $foto;
	}
	salvarFotos($fotos);
}
header('Location: ../consultar/praga.php');
exit;
?>