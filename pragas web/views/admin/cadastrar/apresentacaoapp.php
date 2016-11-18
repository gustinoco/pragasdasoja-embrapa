<?php 

require_once '../restrito.php';
include '../../template/cabecalho.php';
include '../../../dao/ApresentacaoDAO.php';

$dao = new ApresentacaoDAO();
$praga = $dao->listar();

$titulo='Gerenciar menu Apresentação Mobile';


?>
<!-- CONFERE CAMPOS VAZIOS -->
<script type="text/javascript">

    function confere() {
    	if ( document.getElementById("apresentacao").value == "")  {      
	        alert("Por favor, preencha com algum texto."); 
	        return false;
	    }	    
	    return true;
    }
</script>



<div class="large-3 columns">
	<div class="">
		<?php include '../../plugin/menu_lateral_admin.php'; ?>
	</div>
</div>


<!-- PARTE DE CADASTRO -->
<div class="large-9 columns">
	<center><h4 id="titulo" class="panel"><?php echo $titulo?></h4></center>
	<form action="salvar_apresentacao.php" method="post" onSubmit="return confere()" >
		<!-- ID, só existirá se estiver editando, portando fica escondido. tipo 'hidden'-->
			<br><label>Descrição:
				<textarea id="texto" name="texto" style="width:100%">
					<?php echo $praga->texto;?>
				</textarea></label>
		<div class="large-5 columns">			
			<!--SALVAR-->
			<center><input  class="button bt-salvar" type="submit" value="Salvar"></center>
		</div>	
	</form>
</div>

<!-- SCRIPT GERAR EDITORES DE TEXO -->
<script type="text/javascript" src="../../../resource/js/tinymce/tinymce.min.js"></script>
<script type="text/javascript">
//desabilita botao de salvar ao ser clicado
$("#btSalvar").click(function (){
                // desabilita o campo 
        $("#btSalvar").prop("disabled", true);
});
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