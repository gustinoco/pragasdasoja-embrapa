<?php 

include '../restrito.php';
include '../../template/cabecalho.php';
include '../../../dao/FeedbackDAO.class.php';

$dao = new FeedbackDAO();


if(isset($_POST['id']) AND ($_POST['id']>0)){
	$dao->deletar($_POST['id']);
}
$feeds = $dao->listar();
?>


<!-- CRIA MENU LATERAL -->
<div class="large-3 columns">
	<div class="">
		<?php include '../../plugin/menu_lateral_admin.php'; ?>
	</div>
</div>
<!-- FIM MENU LATERAL -->
<!-- PARTE DE CADASTRO -->
<div class="large-9 columns"><div class="panel">
	<center><h4 id="titulo" class="panel">Consultando todos os feedback</i></h4></center>
	<table style="width:100%;">
		<tr>
			<th id="col-tab">id</th>
			<th id="col-tab">nome usuario</th>	
			<th id="col-tab">email</th>	
			<th id="col-tab">data</th>	
			<th id="col-tab"><center>descricao</center></th>	
			<th id="col-tab"><center>remover</center></th>	
		</tr>
		<?php
		if($feeds)
		foreach ($feeds as $f) { ?>
		<tr>
			<td><?php echo $f->id; ?></td>
			<td><?php echo $f->nomeUsuario; ?></td>
			<td><?php echo $f->email; ?></td>
			<td><?php echo $f->data; ?></td>
			<td><?php echo $f->descricao; ?></td>
			<!--FORMULARIO REMOVER-->
			<form action="" method="post" onSubmit="return deletar()">
				<input type="hidden" name="id" value=<?php echo "'$f->id'";?>>
				<td><center><input type="submit" class="button bt-remover" value="x"></input></center></td>
			</form>
		</tr>
		<?php }?>
	</table>
</div>
<?php
include '../../template/rodape.php';
?>
