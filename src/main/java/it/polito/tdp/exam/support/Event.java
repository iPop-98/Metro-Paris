package it.polito.tdp.exam.support;

import java.time.LocalTime;

public class Event implements Comparable<Event> {
	
	public enum EventType {
		NEW,
		TRIAGE,
		TIMEOUT,
		FREE_STUDIO,
		HEAL
	}
	
	private LocalTime time ;
	private EventType type ;
	private GeneralObject obj ;
	
	public Event(LocalTime time, EventType type, GeneralObject obj) {
		super();
		this.time = time;
		this.type = type;
		this.obj = obj;
	}

	public LocalTime getTime() {
		return time;
	}

	public EventType getType() {
		return type;
	}

	public GeneralObject getPatient() {
		return obj;
	}

	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time) ;
	}

	@Override
	public String toString() {
		return "[" + time + "-" + type + ": " + obj + "]";
	}
	
	

}