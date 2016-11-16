package br.embrapa.cpao.pragas.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import br.embrapa.cpao.pragas.R;
import br.embrapa.cpao.pragas.fragments.Fragmento_ListaPragas;


/**
 * Atividade Principal, contêm menu lateral (<i>navdrawer</i>), corpo principal onde pode ser instanciado fragmentos, topbar Menu com opções de menu.
 * Atividade sempre é chamado ao iniciar a aplicação exceto quando a aplicação é recem instalada,
 * neste caso quem será chamado será a atividade de Apresentação {@link ActivityApresentacao}
 * @author Juarez Arce Franco Junior
 * @category atividade
 * @version 1.0
 *
 */
public class ActivityMain extends ActivityApp implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/** Fragmmento do menu Lateral esquerdo */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	Fragmento_ListaPragas fragmento_listaPragas = new Fragmento_ListaPragas();

	/**
	 * É a primeira função a ser executada na Activity.
	 * É responsável por carregar os layouts XML e outras operações de inicialização.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//atuliza action bar.
		String titulo = " ";
		updateActionBar(titulo);
		ActionBar toolbar = getSupportActionBar();
		toolbar.setDisplayShowTitleEnabled(false);
		//cria instancia do fragmento que exibirá lista com todas as pragas
		//fragmento_listaPragas = new Fragmento_ListaPragas() ;

		//cria menu lateral esquerdo, o que exibe ao arrastar com o dedo
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

	}

	@Override
	protected void onResume(){
		super.onResume();
		fragmento_listaPragas.refreshList();
	}

	@Override
	public void onBackPressed(){
		moveTaskToBack(true);
	}
	/**
	 * Evento de clique de um item do menu lateral.
	 *
	 */
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		if(position==0){
			fragmentManager.beginTransaction().replace(R.id.container,fragmento_listaPragas).commit();
		}

	}


	/**
	 * É responsável por carregar o menu XML e inflar na Activity.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//infla o menu_main encontrado na pasta resource/menu
		getMenuInflater().inflate(R.menu.menu_main, menu);


		MenuItem itemPesquisar = menu.findItem(R.id.action_pesquisar);//faz o cast do item de pesquisa

		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			onCreateSearchView(itemPesquisar);
		    return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Cria searchview, que é o componente de pesquisa que fica posicionado no actionbar da activity
	 * a linha abaixo deve ser colocado dentro do item do menu no xml para o MenuItem tem o formato de um SearchView
	 * 		app:actionViewClass="android.support.v7.widget.SearchView"
	 *
	 */
	private void onCreateSearchView(MenuItem itemMenu){

		//faz o cast do menu
		SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(itemMenu);
		//Define um texto de fundo (hint):
		mSearchView.setQueryHint("nome da praga");

		// evento do searchView
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				//método chamado quando o botão para pesquisar é pressionado
				return false;
			}
			/**
			 * Método executa toda vez que um caractere é entrado. Fazendo assim uma nova pesquisa automatica.
			 */
			@Override
			public boolean onQueryTextChange(String query) {
				//filtra pragas
				if(fragmento_listaPragas!=null)
                    //passa responsábilidade para o fragmento que contem a lista das pragas, ele deve fazer a filtragem
					fragmento_listaPragas.filtrarListaPragas(query);
				return false;
			}
		});
	}




	/**
	 * Função responsável por tratar eventos de clique no menu da Activity.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings){
			Intent intent = new Intent(this, ActivityOpcoes.class);
			startActivity(intent);
		}
		if(id == R.id.action_enviar_praga){
			Intent intent = new Intent(this, ActivityEnviarPragaDesconhecida.class);
			startActivity(intent);
		}
		if (id == R.id.action_sair){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}


}
