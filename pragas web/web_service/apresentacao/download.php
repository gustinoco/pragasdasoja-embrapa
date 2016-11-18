<?php
/**
  * PÁGINA RESPONSÁVEL POR LISTAR TODAS OS AUTORES E RETORNAR INFORMAÇÕES NO FORMATO DE JSON
  * PARA QUE USUÁRIO QUE REQUISITOU CONTEÚDO POSSA FAZER A LEITURA.
  */

include_once $_SERVER['DOCUMENT_ROOT'].'/dao/Apresentacao.class.php';

//instancia classe que possui método que comunicam com o BD
$dao = new ApresentacaoDao();
//recupera todos os autores 
$autores = $dao->listar();

//mostra para cliente array com todos os autores em forma de json
echo json_encode($autores);

?>


