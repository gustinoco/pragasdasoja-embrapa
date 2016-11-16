<?php 

include '../restrito.php';
include '../../template/cabecalho.php';
include '../../../dao/PragaDAO.class.php';

$dao = new PragaDAO();
/** Verifica se existe algum id que foi passado por POST, se sim, então usuario deseja deletar determinado praga */
if(isset($_POST['id']) AND is_numeric($_POST['id'])){
	$dao->deletar($_POST['id']);
}

$pragas = $dao->listar_basico_pragas();

?>

<div class="large-3 columns">
	<div class="">
		<?php include '../../plugin/menu_lateral_admin.php'; ?>
	</div>
</div>

<div class="large-9 columns">
	<div class="panel">
		<center><h4 id="titulo" class="panel">Consultar Pragas</h4></center>
		<a class="bt-cadastrar" href="../cadastrar/praga.php"> + Cadastrar nova Praga</a>
		<center>
			<table style="width:100%;">
				<tr>
					<th id="col-tab">id</th>
					<th id="col-tab">nome</th>
					<th id="col-tab">nome científico</th>
					<th id="col-tab"><center>editar</center></th>
					<th id="col-tab"><center>remover</center></th>
				</tr>
				<?php 
				if($pragas){
					foreach ($pragas as $praga) {?>
						<tr>
							<td><?php echo $praga->id;?></td>
							<td><?php echo $praga->nome;?></td>
							<td><?php echo $praga->nomeCientifico;?></td>
							<!--FORMULARIO EDITAR-->
							<td><center><a class="button" href="../cadastrar/praga.php?id=<?php echo $praga->id; ?>"> > </a></center></td>
							
							<!--<form action="../cadastrar/praga.php" method="post" >
								<input type="hidden" name="id" value=<?php //echo "'$praga->id'";?>>
								<td><center><input type="submit" class="button bt-editar" value=">"></input></center></td>
							</form>-->
							<!--FORMULARIO REMOVER-->
							<form action="" method="post" onSubmit="return deletar()">
								<input type="hidden" name="id" value=<?php echo "'$praga->id'";?>>
								<td><center><input type="submit" class="button bt-remover" value="x"></input></center></td>
							</form>
						</tr>
						<?php 
					}
				}
				?>
		</table></center>
	</div>
</div>

<?php
include '../../template/rodape.php';
?>