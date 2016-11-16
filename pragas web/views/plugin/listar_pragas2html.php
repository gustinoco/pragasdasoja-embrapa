<?php
/**
  * ESSE ARQUIVO FUNCIONA COMO PLUGIN DE INFORMAÇÕES, BASTA DAR UM INCLUDE NELE
  * E SERÁ ADICIONADA LISTA DE PRAGAS DIVIDIDAS EM 3 COLUNAS
  */

include_once 'conf/connect.php';
include_once 'dao/PragaDAO.class.php';
define("DIR_T","resource/img/thumbnail/");

$dao = new PragaDAO();
$pragas = $dao->listar_basico_pragas();

//Determina limite por coluna
$limit = count($pragas)/3;
$cont=0;

if($pragas){
	?>
	<div class="row">
	<?php
    /* CRIA LISTA DAS PRAGAS */
    foreach ($pragas as $praga) {
		//foto
		if($praga->descricao_1foto!=null){
		$src = DIR.$praga->id.'-'.$praga->descricao_1foto.'.jpg';
		$img = "<img style='max-height: 50px;' src= '$src' />";
		}else{
		$img='<img src="http://placehold.it/80x80&text=[img]"/>';
		}
		
		//<!--Mostra foto, nome e descrição, são criado 3 colunas para mostrar as pragas-->	
		if($cont==0){//cria nova coluna e uma row
			echo '<div class="large-4 columns">';
		}?>
		  <div class="bloco_praga">
		  	<HR WIDTH=100% ALIGN=RIGHT NOSHADE>
				<div class="large-5 columns small-3" style="padding-bottom:50px;">
					<a href=<?php echo "'/views/praga.php?id=$praga->id'";?>>
					<?php echo $img;?></div></a>
				<div>
				<p><strong><span class="corTema">
				  <a href=<?php echo "'views/praga.php?id=$praga->id'";?>>
				  	<h5><?php echo $praga->nome; ?></h5>
				  	<span style="color:#AAA;"><p><?php echo $praga->nomeCientifico; ?></p></span>
				  </a>
				</span></strong>
				  <?php //echo $praga->descricao; ?>
				</p>
				</div>
		  </div>
		<?php 
		$cont++;
		if($cont>=$limit){
			echo '</div>';//fecha coluna
			$cont=0;
		}
		//<!--Fim praga-->
	
	}//FECHA WHILE
	echo '</div>';//fecha row e coluna pai

}//FIM IF
?>
