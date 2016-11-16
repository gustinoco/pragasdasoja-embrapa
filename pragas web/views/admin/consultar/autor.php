<?php 

include '../restrito.php';
include '../../template/cabecalho.php';
include '../../../dao/AutorDAO.class.php';

$dao = new AutorDAO();
/** Verifica se existe algum id que foi passado por POST, se sim, então usuario deseja deletar determinado praga */
if(isset($_POST['id']) AND is_numeric($_POST['id'])){
	$dao->deletar($_POST['id']);
}
$titulo='Cadastrando novo Autor<i>(pesquisador)</i>';

$autores = $dao->listar_info_basico();
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
	<center><h4 id="titulo" class="panel">Consultando Autores<i>(Pesquisadores)</i></h4></center>
	<a class="bt-cadastrar" href="../cadastrar/autor.php"> + Cadastrar novo Autor</a>
	<table style="width:100%;">
		<tr>
			<th id="col-tab">id</th>
			<th id="col-tab">nome</th>	
			<th id="col-tab"><center>editar</center></th>	
			<th id="col-tab"><center>remover</center></th>	
		</tr>
		<?php
		if($autores)
		foreach ($autores as $autor) {?>
			<tr>
				<td><?php echo $autor->id;?></td>
				<td><?php echo $autor->nome;?></td>
				<!--FORMULARIO EDITAR-->
				<form action="../cadastrar/autor.php" method="post" >
					<input type="hidden" name="id" value=<?php echo "'$autor->id'";?>>
					<td><center><input type="submit" class="button bt-editar" value=">"></input></center></td>
				</form>
				<!--FORMULARIO REMOVER-->
				<form action="" method="post" onSubmit="return deletar()">
					<input type="hidden" name="id" value=<?php echo "'$autor->id'";?>>
					<td><center><input type="submit" class="button bt-remover" value="x"></input></center></td>
				</form>
			</tr>
		<?php 
		}?>
		
	</table>
</div>
<?php
include '../../template/rodape.php';
?>
