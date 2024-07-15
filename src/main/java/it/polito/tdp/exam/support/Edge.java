package it.polito.tdp.exam.support;

import java.util.Objects;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;


public class Edge {
	
	private GeneralObject origin;
	private GeneralObject destination;
	private double peso; // int o qualsiati altro tipo sia necessario
	
	
	public Edge(GeneralObject origin, GeneralObject destination, double peso) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.peso = calcolaPeso();
	}


	public GeneralObject getOrigin() {
		return origin;
	}
	
	
	public GeneralObject getDestination() {
		return destination;
	}
	
	
	public double getPeso() {
		return peso;
	}
	
	/**
	 * Questa funzione, note le coordinate LatLng di due punti, calcola e restituisce il valore della distanza nell'unità riportata di seguito.
	 * @return valore della distanza, calcolato in metri
	 */
	public double calcolaPeso() {
		double distanza = LatLngTool.distance(this.origin.getCoords(),
				this.destination.getCoords(), LengthUnit.METER) ;
		return distanza;
	}


	@Override
	public int hashCode() {
		return Objects.hash(destination, origin, peso);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		return Objects.equals(destination, other.destination) && Objects.equals(origin, other.origin)
				&& Double.doubleToLongBits(peso) == Double.doubleToLongBits(other.peso);
	}
	
	

}
