<?php
require_once 'restrito.php';

include '../template/cabecalho.php';

?>

<div class="large-3 columns">
	<div class="">
		<?php include '../plugin/menu_lateral_admin.php'; ?>
	</div>
</div>

<div class="large-9 columns">
	<div class="panel">
		<center><h1>Olá <?php echo $_SESSION['usuarioNome'];?>, bem-vindo à área administrativa</h1>
			<p>:)</p></center>
	</div>
</div>

<?php
include '../template/rodape.php';
?>