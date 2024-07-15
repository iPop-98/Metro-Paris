package it.polito.tdp.exam.support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;


public class GeneralObject {
	
	public enum State {
		UNKNOWN, WHITE, YELLOW, RED, BLACK, HEALING, OUT ;
	}
	
	private Integer objectID;	// quando creo un'attributo di tipo private, è importante chiedermi cosa è possibile con questa variabile:
								// se posso modificarla dall'esterno, creare un appossito setter;
								// se devo poterne leggere il valore in altre parti del programma, devo predisporre un getter;
								// IMPORTANTISSIMO: se decidecci che su quest'attributo si regge il corrispettivo HashCode per il metodo equals, allora questo campo dev'essere immutabile.
	
	private String name;
	
	private LatLng coords;
	
	private LocalDate dataCreazione;		// quando si ha una variabile di questo tipo e la si vuole leggere da un DB che contiene variabili di tipo DATE, bisogna avvalersi del metodo .toLocalDate; al contrario
											// se si vuole passare una variabile di questo tipo al DB, sfruttare il metodo .toDate
	private LocalDateTime dataEoraUltimoUtilizzo;	//quando si ha una variabile di questo tipo e la si vuole leggere da un DB che contiene variabili di tipo DATE, bisogna avvalersi del metodo .toLocalDateTime
	private LocalTime oraAttuale;
	
	
	private LocalTime simStart = LocalTime.of(8, 0);
	private LocalTime simStop = LocalTime.of(20, 0);
	
	
	public GeneralObject(Integer objectID, String name, LatLng coords, LocalDate dataCreazione,
			LocalDateTime dataEoraUltimoUtilizzo, LocalTime oraAttuale) {
		super();
		this.objectID = objectID;
		this.name = name;
		this.coords = coords;
		this.dataCreazione = dataCreazione;
		this.dataEoraUltimoUtilizzo = dataEoraUltimoUtilizzo;
		this.oraAttuale = oraAttuale;
	}

 
	//Costruttore per il simulatore
	public GeneralObject(State unknown, LocalTime ora) {
		// TODO Auto-generated constructor stub
	}


	public Integer getPrimaryKey() {
		return objectID;
	}
	
	
	public LatLng getCoords() {
		return coords;
	}

	/**
	 * Dato un secondo oggetto obj2, che ha un attributo LanLng, è possibile calcolare la distanza attraverso un metodo di utilità di questa classe di oggetti
	 * @param obj2
	 * @return distance
	 */
	public double getDistanza(GeneralObject obj2) {
		double distanza = LatLngTool.distance(this.getCoords(), obj2.getCoords(), LengthUnit.METER) ;
		return distanza;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(objectID);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneralObject other = (GeneralObject) obj;
		return Objects.equals(objectID, other.objectID);
	}


	public void setState(State red) {
		// TODO Auto-generated method stub
		
	}


	public void setArrival(LocalTime time) {
		// TODO Auto-generated method stub
		
	}


	public Object getState() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean comesBefore(GeneralObject best) {
		// TODO Auto-generated method stub
		return false;
	}		

	
	
}
