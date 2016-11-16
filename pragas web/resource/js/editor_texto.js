include ('tinymce/tinymce.min.js');

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