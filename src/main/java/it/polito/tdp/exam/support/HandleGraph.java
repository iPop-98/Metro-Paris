package it.polito.tdp.exam.support;


import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.jgrapht.Graph;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.alg.connectivity.ConnectivityInspector;

public class HandleGraph {

	private List<GeneralObject> oggetti;
	private Map<Integer, GeneralObject> oggettiIdMap ;	// Questo tipo di struttura dati è di suporto in quelle situazioni in cui, ad esempio, dall'interfaccia utente viene passato 
														// l'
	
	private Graph<GeneralObject, DefaultEdge> grafo1 = new SimpleGraph<>(DefaultEdge.class);
	private Graph<GeneralObject, DefaultWeightedEdge> grafo2;
	private Graph<GeneralObject, DefaultWeightedEdge> grafo3 = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
	
	public void initializeSimpleGraph() {
		
	}
	
	public void initializeSimpleWeightedGraph() {
		// crea l'oggetto grafo
		this.grafo2 = new SimpleWeightedGraph<GeneralObject, DefaultWeightedEdge>(DefaultWeightedEdge.class) ;

		// aggiungi i vertici
		DAO dao = new DAO() ;
		this.oggetti = dao.getAllObjects() ;

		oggettiIdMap = new HashMap<>();
		for(GeneralObject obj: this.oggetti)
			this.oggettiIdMap.put(obj.getPrimaryKey(), obj) ;

		Graphs.addAllVertices(this.grafo2, this.oggetti) ;  //metodo di utilità per un'aggiunta più rapida di tutti i vertici, anzhiché aggiungerli uno alla volta, con il metodo apposito della classe Graph

		// metodo 3: faccio una query per prendermi tutti gli edges 

		List<Edge> allArchi = dao.getAllArchi(oggettiIdMap);
		for (Edge arco : allArchi) {
			//DefaultWeightedEdge e = this.grafo.addEdge(arco.getPartenza(), arco.getArrivo());
			//if (e!=null)
				// this.grafo.setEdgeWeight(e, arco.getPeso());

			Graphs.addEdge(this.grafo2, arco.getOrigin(), arco.getDestination(), arco.getPeso()) ;
			// Graphs.addEdge(this.grafo2, arco.getOrigin(), arco.getDestination(), 1.0) ;
		}

		System.out.println("Grafo creato con "+this.grafo2.vertexSet().size() + " vertici e " + this.grafo2.edgeSet().size() + " archi") ;
		System.out.println(this.grafo2);
	}
	
	public void initializeSimpleDirectedWeightedGraph() {
		
	}
	
	/**
	 * Metodo di controllo che serve a verificare se il grafo è stato inizializzato correttamente
	 * @return true se ha almeno 2 vertici o un arco
	 * @return false altrimenti
	 */
	public boolean isGrafoLoaded() {
		return (this.grafo1.vertexSet().size()>2 && this.grafo1.edgeSet().size()>0);
	}
	
	/**
	 * Determina il percorso minimo tra le 2 fermate di un SimpleGraph con DefaultEdge
	 * @param partenza 
	 * @param arrivo
	 * @return List di vertici che compongono il percorso minimo
	 */
	public List<GeneralObject> percorso(GeneralObject partenza, GeneralObject arrivo) {

		//Metodo imprementando il BFS Tree - CONSIGLIATO CON GRAFI NON PESATI: se pesato, trova il cammino con minor numero di archi, ma non è detto che sia quello di peso minimo - sia per grafi semplici che orientati
		//si parte creando l'albero di visita, avente come radice @param partenza, che ci darà tutti i cammini minimi, verso qualunque nodo raggiungibile da partenza, rispetto al grafo passatoli
		BreadthFirstIterator<GeneralObject, DefaultEdge> visita = new BreadthFirstIterator<>(this.grafo1, partenza);
		
		//Se volessimo conoscere l'intero contenuto (ed eventualmente la numerosità della componente connessa),
		//possiamo popolare una lista di supporto con i risultati dati dall'iteratore (e quindi calcolandone la size)
//			List<GeneralObject> reachable = new ArrayList<GeneralObject>();
			while(visita.hasNext()) {
				/*GeneralObject nodo =*/ visita.next();
				//reachable.add(nodo);
			}
//			//return reachable.size();
			//Altrimenti vedere metodo calcolaConnessa, in cui sono proposte delle varianti
				
		
		//Una volta creato l'albero di visita, posso interrogarlo per esplorarlo, avvalendoci del metodo .getSpanningTreeEdge(Vertex v), il quale, dato un vertice,
		//restituisce l'arco da percorrere per potervi arrivare. Iterando l'operazione, partendo dal vertice @param arrivo, e andando a ritroso, fino a @param partenza (nodo radice),
		//otteniamo il percorso (al contrario) da seguire per andare da un capo all'altro
		List<GeneralObject> trip = new LinkedList<>();
		GeneralObject corrente = arrivo;
		trip.add(arrivo);
		while(!corrente.equals(partenza)) {
			DefaultEdge e = visita.getSpanningTreeEdge(corrente);
			GeneralObject precedente = Graphs.getOppositeVertex(this.grafo1, e, corrente);	//metodo di utilità che, dato un arco e un suo estremo, restiuisce il vertice opposto
			trip.add(0, precedente); //variante del metodo add, che permette di aggiungere in testa, per compensare l'ordinamento al contrario del percorso
									// NOTA BENE: questa operazione risulta FATTIBILE con una LinkedList, NO con ArrayList (bassa efficenza computazionale); alternativa sarebbe usare una coda.
			corrente = precedente;
		}
		
		
		return trip;
	}
	
	
	/**
	 * Determina il percorso minimo tra le 2 fermate, esaminando un grafo PESATO
	 * @param partenza 
	 * @param arrivo
	 * @return List di vertici che compongono il percorso minimo
	 */
	public List<GeneralObject> percorsoPesato(GeneralObject partenza, GeneralObject arrivo) {

		//metodo con algoritmo di Dijkstra; SOLO grafi con archi aventi tutti  peso>=0; immediato e compatto nel codice; non mi richiede la ricomposizione del percorso
		DijkstraShortestPath<GeneralObject, DefaultWeightedEdge> sp = 
				new DijkstraShortestPath<>(this.grafo2) ;
		
		GraphPath<GeneralObject, DefaultWeightedEdge> gp = sp.getPath(partenza, arrivo) ;
		
		return gp.getVertexList() ;
	}
	
	
	/**
	 * Questo metodo serve a calcolare il numero di nodi raggiungibili a partire da un nodo di partenza, dato come primo elemento d'ingresso del metodo stesso, 
	 * all'interno di un grafo, da passare come secondo paramento
	 * @param obj : vertice da cui si vuole partire nel calcolo della componente connessa
	 * @param grafo : all'interno del quale svolgere la ricerca
	 * @return int : numero di nodi raggiungibili
	 */
	public Integer calcolaConnessa(GeneralObject obj, Graph grafo) {
		int nodiConnessi;
		// Modo 1: esploro il grafo, e calcolo la size del set dei vertici esplorati 
		// dall'iteratore (garanzia di aver esplorato tutti i nodi connessi)
		
		DepthFirstIterator<GeneralObject, DefaultWeightedEdge> iterator = new DepthFirstIterator<>(grafo, obj); // metodo di visita in profondità
		
		List<GeneralObject> compConnessa = new ArrayList<>();
		
		while(iterator.hasNext())	//iteratore che, come il ResultSet, punta alla riga di memoria prima del primo elemento presente
			compConnessa.add(iterator.next());
		nodiConnessi = compConnessa.size();
//		return nodiConnessi;
		
		// Modo 2: uso la classe ConnectivityInspector che già implementa un metodo per recuperare tutti i nodi connessi
		// ad un nodo radice, dato  come Set. Preferibile 
		
		ConnectivityInspector<GeneralObject, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(grafo);
		Set<GeneralObject> setConnesso = inspector.connectedSetOf(obj);

		return setConnesso.size();
	}
}
