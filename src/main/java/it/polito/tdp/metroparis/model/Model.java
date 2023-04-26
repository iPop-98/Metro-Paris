package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata, DefaultEdge> grafo ;
	private List<Fermata> fermate ;
	private Map<Integer, Fermata> fermateIdMap ;
	
	public void creaGrafo() {
		
		// crea l'oggetto grafo
		this.grafo = new SimpleGraph<Fermata, DefaultEdge>(DefaultEdge.class) ;
		
		// aggiungi i vertici
		MetroDAO dao = new MetroDAO() ;
		this.fermate = dao.readFermate() ;
		
		this.fermateIdMap = new HashMap<Integer, Fermata>();
		for(Fermata f: this.fermate)
			this.fermateIdMap.put(f.getIdFermata(), f) ;
		
		Graphs.addAllVertices(this.grafo, this.fermate) ;
		
		// aggiungi gli archi
		
		// metodo 1: considero tutti i potenziali archi
//		long tic = System.currentTimeMillis();
//		for(Fermata partenza: this.grafo.vertexSet()) {
//			for(Fermata arrivo: this.grafo.vertexSet()) {
//				if(dao.isConnesse(partenza, arrivo)) {
//					this.grafo.addEdge(partenza, arrivo) ;
//				}
//			}
//		}
//		long toc = System.currentTimeMillis();
//		System.out.println("Time elapsed for mode 1 "+ (toc-tic));
//		
		
		// metodo 2: data una fermata, trova la lista di quelle adiacente
		long tic = System.currentTimeMillis();
		for(Fermata partenza: this.grafo.vertexSet()) {
			List<Fermata> collegate = dao.trovaCollegate(partenza, fermateIdMap) ;
			
			for(Fermata arrivo: collegate) {
				this.grafo.addEdge(partenza, arrivo) ;
			}
		}
		long toc = System.currentTimeMillis();
		System.out.println("Time elapsed for mode 2 "+ (toc-tic));
		
		
		// metodo 2bis: data una fermata, trova la lista degli id adiacenti
		tic = System.currentTimeMillis();
		for(Fermata partenza: this.grafo.vertexSet()) {
			List<Integer> collegateId = dao.trovaIdCollegate(partenza, fermateIdMap) ;
			
			for(Integer idArrivo: collegateId) {
				this.grafo.addEdge(partenza, fermateIdMap.get(idArrivo));
			}
		}
		toc = System.currentTimeMillis();
		System.out.println("Time elapsed for mode 2bis "+ (toc-tic));
		
		
		// metodo 3: carico direttamente tutti gli edges
		tic = System.currentTimeMillis();
		List<coppiaF> allCoppie = new ArrayList<>();
		allCoppie = dao.getAllCoppie(fermateIdMap);
		
		for (coppiaF coppia : allCoppie) {
			this.grafo.addEdge(coppia.getPartenza(),coppia.getArrivo());
		}
		toc = System.currentTimeMillis();
		System.out.println("Time elapsed for mode 3 "+ (toc-tic));
		
		
		
		System.out.println("Grafo creato con "+this.grafo.vertexSet().size() +
				" vertici e " + this.grafo.edgeSet().size() + " archi") ;
		System.out.println(this.grafo);
	}

	public List<Fermata> getAllFermate(){
		MetroDAO dao = new MetroDAO() ;
		return dao.readFermate();
	}
	
	public Boolean isGrafoCreato () {
		return this.grafo.vertexSet().size()>0;
	}
}
