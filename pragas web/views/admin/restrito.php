<?php 
if(!isset($_SESSION))
	session_start();
if(!isset($_SESSION['usuarioID'])){
	session_destroy();
	header("Location: ../../../../views/admin/login.php"); 
	exit;
}
?>