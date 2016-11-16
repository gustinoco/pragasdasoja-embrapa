<?php


include '../../dao/PragaDAO.class.php';

$dao = new PragaDAO();

$ids = json_encode($dao->listaIdsPragasExistentes());
echo $ids;