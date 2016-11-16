<?php
/** PAGINA RECEBE FEEDBACK */
include_once '../../conf/connect.php';
include_once '../../dao/FeedbackDAO.class.php';
include_once '../../model/Feedback.class.php';

if(empty($_POST)){
	die("falha");
}

$obj->nomeUsuario=$_POST['nomeUsuario'];
$obj->email = $_POST['email'];
$obj->classificacao= $_POST['classificacao'];
$obj->descricao = $_POST['descricao'];
$obj->data = date("Y-m-d H:i:s");

//instancia objeto feedback
$fb = new Feedback();
$fb->mapear($obj);


//salva objeto
$dao = new FeedbackDAO();
if($dao->salvar($fb)) 
	echo 'sucesso';
else 
	echo 'falha';

?>