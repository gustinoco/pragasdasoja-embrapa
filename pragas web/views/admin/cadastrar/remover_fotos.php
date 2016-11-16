<?php

require_once '../restrito.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/dao/PragaDAO.class.php';


$ids_fotos = $_POST['fotos'];
$dao=new PragaDAO();


if($ids_fotos){
	$dao->remover_fotos($ids_fotos);
}else{
	echo 'nenhum foto foi selecionada';
}
header('Location: ../cadastrar/praga.php?id='.$_POST['id']);


