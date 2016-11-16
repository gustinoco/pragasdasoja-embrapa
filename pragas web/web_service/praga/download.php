<?php
/**
  * PÁGINA RESPONSÁVEL POR LISTAR TODAS AS PRAGAS E RETORNAR INFORMAÇÕES NO FORMATO DE JSON
  * PARA QUE USUÁRIO QUE REQUISITOU CONTEÚDO POSSA FAZER A LEITURA.
  */

include_once $_SERVER['DOCUMENT_ROOT'].'/dao/PragaDAO.class.php';

//recupera map em forma de json com chave(id da praga) e valor(versao da praga)
$map = $_GET['map'];
if($map==null) die('falha');

//recupera array do json
$map = (array)json_decode($map);

//instancia classe dao que possui métodos que interagem com BD
$dao = new PragaDAO();
//busca pragas que cliente nao possui e pragas mais atualizadas das que o cliente tem
$pragas = $dao->filtrarPragas($map);

//mostra json de todas as pragas, cliente fara a leitura do json.
echo json_encode($pragas);
?>