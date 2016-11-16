<?php 

require_once '../restrito.php';
include_once '../../template/cabecalho.php';
include_once '../../../dao/AutorDAO.class.php';

$dao = new AutorDAO();

$titulo='Cadastrando novo Autor<i>(pesquisador)</i>';

/** VERIFICA SE FOI PASSADO ALGUM ID POR POST, SE SIM, ENTÃO USUARIO DESEJA ALTERAR O CONTEÚDO*/
if(isset($_POST['id']) AND is_numeric($_POST['id'])){
	$dao=new AutorDAO();
	$autor = $dao->buscar_autor($_POST['id']);
	$titulo='Editando autor "'.$autor->nome.'"';
}

?>
<!-- CONFERE CAMPOS VAZIOS -->
<script type="text/javascript">

    function confere() {
    	if ( document.getElementById("nome").value == "")  {      
	        alert("Por favor, preencha o nome."); return false;
	    }
	    return true;
    }
</script>
<!-- SCRIPT VERIFICA CAMPOS VAZIOS -->

<!-- CRIA MENU LATERAL -->
<div class="large-3 columns">
	<div class="">
		<?php include '../../plugin/menu_lateral_admin.php'; ?>
	</div>
</div>
<!-- FIM MENU LATERAL -->

<!-- PARTE DE CADASTRO -->
<div class="large-9 columns">
	<center><h4 id="titulo" class="panel"><?php echo $titulo?></h4></center>
	<form action="salvar_autor.php" method="post" onSubmit="return confere()" enctype="multipart/form-data">
		<!-- ID, só existirá se estiver editando, portando fica escondido. tipo 'hidden'-->
		<input type="hidden" name="id" value=<?php echo "'$autor->id'";?>></input>
		<div class="large-5 columns">
			<!--FOTO UPLOAD-->
			<label>Foto: (.jpg)
			<input type="file" name="foto" /></label>
			<!-- NOME -->
			<label>Nome:
			<input  id="nome" name="nome" type="text" placeholder="nome do pesquisador" 
					value="<?php echo $autor->nome;  ?>"></input></label>

			<label>Email:
			<input  name="email" type="text" placeholder="exemplo@exemplo.com" 
				value="<?php echo $autor->email;?>"></input></label>
			<label>Telefone:
			<input  name="telefone" type="text" 
				value="<?php echo $autor->telefone;?>"></input></label>
			<label>Telefone2:
			<input  name="telefone2" type="text"
				value="<?php echo $autor->telefone2;?>"></input></label>
			<!--DESCRICAO-->
			<br><label>Breve descrição:
			<textarea name="descricao" style="width:100%">
				<?php echo $autor->descricao;?>
			</textarea></label>
				
			<center><input  class="button bt-salvar" type="submit" value="Salvar"></center>
		</div>

	
	</form>
</div>
<!-- SCRIPT GERAR EDITORES DE TEXO -->
<script type="text/javascript" src="../../../resource/js/tinymce/tinymce.min.js"></script>
<script type="text/javascript">
tinymce.init({
	selector: "textarea",
	language: "pt_BR",
	theme: "modern",
	plugins: [
	"advlist autolink lists link charmap print preview hr pagebreak",
	"searchreplace wordcount visualblocks visualchars code fullscreen",
	"insertdatetime  nonbreaking save table contextmenu directionality",
	"emoticons template paste textcolor colorpicker textpattern"
	],
	toolbar1: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link | print preview  | forecolor backcolor",
	toolbar2: "",
	image_advtab: true,
	templates: [
	{title: 'Test template 1', content: 'Test 1'},
	{title: 'Test template 2', content: 'Test 2'}
	]
});
</script>
<!-- FIM SCRIPT GERAR EDITOR DE TEXTO-->

<?php
include '../../template/rodape.php';
?>
