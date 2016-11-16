<?php
include_once __DIR__.'/../conf/connect.php';
include_once __DIR__.'/../dao/PragaDAO.class.php';
define("DIR_T","../resource/img/thumbnail/");

$strPesquisa = $_GET['pesquisar'];

$dao = new PragaDAO();
$pragas = $dao->filtrar_pragas($strPesquisa);

include 'template/cabecalho.php';

//verifica se houve resultado
if($pragas){
	$total = count($pragas);?>
	<div class='row'>
		<?php 
		if($total==1) $str='resultado';
		else $str='resultados';
		echo "<p style='text-align:right;'><b><i>$total $str</i></b></p>";?>
	</div>
	<?php
	foreach ($pragas as $praga) {
		if($praga->descricao_1foto!=null){
		    //recupera caminho da foto
	        $src_t = DIR_T.$praga->descricao_1foto;
			$img = "<img src= '$src_t' />";
		}else{
			$img='<img src="http://placehold.it/80x80&text=[img]"/>';
	}?>
	<div class="row">
		<div class="large-12 columns">

		  <div class="large-3 columns small-3"><?php echo $img;?></div>
		  <div>
		    <p><strong><span class="corTema">
		      <a href=<?php echo "'praga.php?id=$praga->id'";?>><h5><?php echo $praga->nome; ?>: </h5></a>
		    </span></strong>
		      <?php echo $praga->descricao; ?>
		    </p>
		      <a class='button btMais' href=<?php echo "'praga.php?id=$praga->id'";?>>mais...</a>
		  </div>
		  <HR WIDTH=100% ALIGN=RIGHT NOSHADE>
  		</div>
	</div>
<?php
}//FECHA WHILE
}//FIM IF
else{
	echo '<center><br><h1>Ops :(</h1><h2>Nenhuma praga foi encontrada!</h2></center><br><br><br>';	
}

include 'template/rodape.php';
?>
