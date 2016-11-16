<?php
include '../../dao/PragaDAO.class.php';

$map = $_GET['map'];
if($map==null) die('falha');

//recupera array do json
$map = (array)json_decode($map);

$dao = new PragaDAO();

$total = json_encode($dao->getCountPragas($map));
echo $total;


?>