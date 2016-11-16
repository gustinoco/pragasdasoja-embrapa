<?php
/**
  * ESSE ARQUIVO FUNCIONA COMO PLUGIN DE INFORMAÇÕES, BASTA DAR UM INCLUDE NELE
  * E SERÁ ADICIONADO UMA GALERIA DE FOTOS DE TODAS AS PRAGAS
  */

include_once 'conf/connect.php';
include_once 'dao/PragaDAO.class.php';
include_once 'resource/libs/wideimage/WideImage.php';
//diretorio das fotos em miniatura
define("DIR","resource/img/thumbnail/");



$dao = new PragaDAO();
$pragas = $dao->listar_basico_pragas();

if($pragas){
  $menuImgs="";
  foreach ($pragas as $praga) {
    //recupera caminho da foto
    $src = DIR.$praga->id.'-'.$praga->descricao_1foto.'.jpg';
    if($praga->descricao_1foto!=null)
      $menuImgs .= "<a href='views/praga.php?id=$praga->id'>
                      <img class='foto_do_menu' src= '$src'/>
                    </a>";
    else{
      $menuImgs .= "<a href='views/praga.php?id=$praga->id'>
                      <img src='http://placehold.it/80x80&text=[img]'/>
                    </a>";
    }
  }?>
  <div class="row">
    <div class="large-12 columns " style='margin: 0 auto;'>
      <div class="menu_fotos">
        <?php echo $menuImgs; ?>
      </div>
    </div>
  </div> 
<?php
}

?>