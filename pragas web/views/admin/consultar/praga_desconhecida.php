<?php 

include '../restrito.php';
include '../../template/cabecalho.php';
include '../../../dao/UsuarioRetornoDAO.class.php';
define('DIR_F','resource/img/fotos/praga_desconhecida/');
define('DIR_T','/resource/img/fotos/praga_desconhecida/thumbnail/');

$dao = new UsuarioRetornoDAO();

if(isset($_POST['id']) AND ($_POST['id']>0)){
	$dao->deletar($_POST['id']);
}

$retornos = $dao->listar();

?>
<!-- SCRIPT DA GALERIA DE FOTOS -->
<script src="../../../resource/js/foundation/foundation.clearing.js"></script>
<script src="../../../resource/js/vendor/modernizr.js"></script>

<!-- CRIA MENU LATERAL -->
<div class="large-3 columns">
	<div class="">
		<?php include '../../plugin/menu_lateral_admin.php'; ?>
	</div>
</div>
<!-- FIM MENU LATERAL -->
<!-- PARTE DE CADASTRO -->
<div class="large-9 columns"><div class="panel">
	<center><h4 id="titulo" class="panel">Consultando todos os envios de pragas desconhecidas</i></h4></center>
	<table style="width:100%;">
		<tr>
			<th id="col-tab">imagens</th>
			<th id="col-tab">id</th>
			<th id="col-tab">nome usuario</th>	
			<th id="col-tab">email</th>	
			<th id="col-tab">data</th>	
			<th id="col-tab"><center>descricao</center></th>
			<th id="col-tab"><center>remover</center></th>	
		</tr>
		<?php
		if($retornos)
		foreach ($retornos as $r) { ?>
		<tr>
			<!--IMAGENS-->
			<td>
			<center><ul style=" margin:0 auto;" data-clearing> 
          		<?php 
          		$fotos = $dao->listarFotos($r->id);
          		foreach ($fotos as $f) {
          			$src='../../../resource/img/fotos/praga_desconhecida/'.$r->id.'_'.$f->id.'.jpeg';
          			$src_t='../../../resource/img/fotos/praga_desconhecida/thumbnail/'.$r->id.'_'.$f->id.'.jpeg';
          			echo "<li><a href='$src' >
		          		       <img class='small-12' src='$src_t'</a>
		          		  </li>";
          		}
          		?>
        	</ul></center>
			</td>
			<!--ID-->
			<td><?php echo $r->id; ?></td>
			<!--NOME-->
			<td><?php echo $r->nomeUsuario; ?></td>
			<!--EMAIL-->
			<td><?php echo $r->email; ?></td>
			<!--DATA-->
			<td><?php echo $r->data; ?></td>
			<!--DESCRICAO-->
			<td><?php echo $r->descricao; ?></td>
			<!--FORMULARIO REMOVER-->
			<form action="" method="post" onSubmit="return deletar()">
				<input type="hidden" name="id" value=<?php echo "'$r->id'";?>>
				<td><center><input type="submit" class="button bt-remover" value="x"></input></center></td>
			</form>
		</tr>
		<?php }?>
	</table>
</div>
<?php
include '../../template/rodape.php';
?>
