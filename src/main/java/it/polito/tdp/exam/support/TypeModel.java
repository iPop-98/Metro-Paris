package it.polito.tdp.exam.support;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import java.util.Collections;	//Interfaccia contenente vari metodi di utilità, che, in taluni casi, permette di semplicare operazioni selle collection, come le List
								//nei casi come ad esempio il .sort()

import java.util.List;			//Array ch epermette di collezionare un insieme di Oggetti, con una serie di metodi per gestirli; *SCARSi per metodi .remove(obj) e .contains(obj) 
import java.util.ArrayList;		//*NO ordinamento; *SCARSO aggiunta posizionale o sorting; *OTTIMA per metodi .get(index) e .set(index, obj)
import java.util.LinkedList;	//Segue ordinamento di inserimento; *ADATTO per metodi .remove() e gestisce meglio aggiunta posizionale e riordino

import java.util.Set;			//Interfaccia di Collection. Da sapere:
								//*NO elementi duplicati, quindi oggetti che devono implementare il metodo equals; *NO accesso posizionale (index); *NO ordinamento
import java.util.HashSet;		//*OTTIMI per metodi 'remove' e 'contains', grazie all'utilizzo della funzione di Hash - gli oggetti devono implementare il metodo 'hashCode()'
import java.util.LinkedHashSet; // >> vaga possibilità di ordinamento, dato un certo link (collegamento) interno tra gli oggetti nel Set

import java.util.Map;			//Struttura che permette di salvare delle coppie Chiave-Valore: *chiave, che indentifica in modo univoco l'oggetto(insieme keySet); *valore, l'oggetto associato (insieme List)
								//Non posso avere chiavi duplicate ma si oggetti duplicati. Le chiavi DEVONO ESSERE IMMUTABILI; *OTTIME per metodi .get(key) e come per i Set, .remove(key) e .contains(key)
								//*NO containsValue(object) perché, a differenza dei Set, l'insieme valore è una lista, quindi da scorrere tutta (operazione lenta)
import java.util.HashMap;		//*NO ordinamento; *RICHIEDE @override dei metodi hashCode() & equals()
import java.util.LinkedHashMap; //*SI ordinamento, secondo l'inserimento, per il resto uguale

public class TypeModel {
	
	
	
	private List<GeneralObject> obj;	//Una List è un interfaccia

	
	private Duration time = Duration.of(28, ChronoUnit.MINUTES);	// con questo tipo di Variabile possiamo inizializzare (metodo of, no costruttore) una certa durata temporale, che potrà essere usata 
																	// con un metodo .plus() o .misus() per aggiungere tale ammontare ad una variabile che contiene in sé un tempo
																	// NOTA BENE: per 'tempo' si intende qualunque tipo di variabile temporale, dai minuti, come in questo caso, ai giorni, mesi, anni e così via
	
	

}
