<?php
include '../../dao/PragaDAO.class.php';

$dao = new PragaDAO();

$total = json_encode($dao->getTotal());
echo $total;


?>