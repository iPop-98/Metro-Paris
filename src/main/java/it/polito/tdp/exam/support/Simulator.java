package it.polito.tdp.exam.support;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.exam.support.Event.EventType;
import it.polito.tdp.exam.support.GeneralObject.State;

public class Simulator {

	// STATO DEL SISTEMA-MONDO
	private List<GeneralObject> patients;
	private int studiLiberi;

	// PARAMETRI DI INGRESSO - TUNING
	private int studiTotali;

	private Duration tArrival = Duration.of(10, ChronoUnit.MINUTES);

	private LocalTime simStart = LocalTime.of(8, 0);
	private LocalTime simStop = LocalTime.of(20, 0);

	private Duration triageTime = Duration.of(5, ChronoUnit.MINUTES);
	private Duration timeoutWhite = Duration.of(60, ChronoUnit.MINUTES);
	private Duration timeoutYellow = Duration.of(60, ChronoUnit.MINUTES);
	private Duration timeoutRed = Duration.of(30, ChronoUnit.MINUTES);

	private Duration healWhite = Duration.of(10, ChronoUnit.MINUTES);
	private Duration healYellow = Duration.of(15, ChronoUnit.MINUTES);
	private Duration healRed = Duration.of(30, ChronoUnit.MINUTES);

	// INDICATORI DI USCITA
	private int nPatients;
	private int nHealedPatients;
	private int nAbandonedPatients;
	private int nDeadPatients;

	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;

	public Simulator(int studiTotali) {
		super();
		this.studiTotali = studiTotali;
		this.studiLiberi = this.studiTotali;
		this.patients = new ArrayList<GeneralObject>();
		this.queue = new PriorityQueue<Event>();
	}

	public void initialize() {
		LocalTime ora = this.simStart;
		while (ora.isBefore(this.simStop)) {
			// simulo un nuovo paziente in quest'ora
			GeneralObject obj = new GeneralObject(State.UNKNOWN, ora);
			this.patients.add(obj);
			this.queue.add(new Event(ora, EventType.NEW, obj));

			ora = ora.plus(this.tArrival);
		}
	}

	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);

			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		LocalTime time = e.getTime();
		GeneralObject obj = e.getPatient();

		switch (e.getType()) {
		case NEW:

			queue.add(new Event(time.plus(this.triageTime), EventType.TRIAGE, obj));
			this.nPatients++;

			break;

		case TRIAGE:
			// assegna un colore e registra il tempo di arrivo in sala d'attesa

			// 50% bianco, 30% giallo, 20% rosso
			double r = Math.random();
			if (r < 0.5)
				obj.setState(State.WHITE);
			else if (r < 0.8)
				obj.setState(State.YELLOW);
			else
				obj.setState(State.RED);
			obj.setArrival(time);
			System.out.println("Triaged: " + obj);

			if (obj.getState().equals(State.WHITE)) {
				this.queue.add(new Event(time.plus(timeoutWhite), EventType.TIMEOUT, obj));
			} else if (obj.getState().equals(State.YELLOW)) {
				this.queue.add(new Event(time.plus(timeoutYellow), EventType.TIMEOUT, obj));
			} else if (obj.getState().equals(State.RED)) {
				this.queue.add(new Event(time.plus(timeoutRed), EventType.TIMEOUT, obj));
			} else {
				throw new IllegalArgumentException("Stato non valido");
			}

			if (this.studiLiberi > 0) {
				this.queue.add(new Event(time, EventType.FREE_STUDIO, null));
			}

			break;

		case TIMEOUT:
			if (obj.getState().equals(State.WHITE)) {
				// va a casa
				obj.setState(State.OUT);
				this.nAbandonedPatients++;
			} else if (obj.getState().equals(State.YELLOW)) {
				// diventa RED
				obj.setState(State.RED);
				obj.setArrival(time);
				this.queue.add(new Event(time.plus(timeoutRed), EventType.TIMEOUT, obj));
			} else if (obj.getState().equals(State.RED)) {
				// diventa BLACK
				obj.setState(State.BLACK);
				this.nDeadPatients++;
			} else {
				// ignoro l'evento perché non è più in attesa
				System.out.println("Timeout ignorato " + obj);
			}

			break;

		case HEAL:
			obj.setState(State.OUT);
			this.studiLiberi++;
			this.nHealedPatients++;
			this.queue.add(new Event(time, EventType.FREE_STUDIO, null));
			break;

		case FREE_STUDIO:

			// verifica che sia davvero uno studio libero
			if (this.studiLiberi > 0) {

				// trova il paziente più urgente da Patients
				// che si trovano in sala d'aspetto
				List<GeneralObject> inSala = new ArrayList<>();
				for (GeneralObject o : this.patients) {
					if (o.getState() == State.WHITE || o.getState() == State.YELLOW || o.getState() == State.RED) {
						inSala.add(o);
					}
				}

				if (inSala.size() > 0) {
					GeneralObject best = inSala.get(0);
					for(GeneralObject o: inSala) {
						if(o.comesBefore(best)) {
							best = o ;
						}
					}

					// occupa lo studio
					this.studiLiberi--;

					// schedula HEAL dopo il tempo di cura
					if(best.getState()==State.WHITE) {
						this.queue.add(
								new Event(time.plus(healWhite), EventType.HEAL, best));
					} else if(best.getState()==State.YELLOW) {
						this.queue.add(
								new Event(time.plus(healYellow), EventType.HEAL, best));
					} else if(best.getState()==State.RED) {
						this.queue.add(
								new Event(time.plus(healRed), EventType.HEAL, best));
					}

					best.setState(State.HEALING);
				} else {
					System.out.println("Nessun paziente in attesa");
				}
			} else {
				System.out.println("Nessuno studio libero");
			}

			break;
		} // switch

	}

	public int getnPatients() {
		return nPatients;
	}

	public int getnHealedPatients() {
		return nHealedPatients;
	}

	public int getnAbandonedPatients() {
		return nAbandonedPatients;
	}

	public int getnDeadPatients() {
		return nDeadPatients;
	}

	public void settArrival(Duration tArrival) {
		this.tArrival = tArrival;
	}

	public void setSimStart(LocalTime simStart) {
		this.simStart = simStart;
	}

	public void setSimStop(LocalTime simStop) {
		this.simStop = simStop;
	}

	public void setTriageTime(Duration triageTime) {
		this.triageTime = triageTime;
	}

	public void setTimeoutWhite(Duration timeoutWhite) {
		this.timeoutWhite = timeoutWhite;
	}

	public void setTimeoutYellow(Duration timeoutYellow) {
		this.timeoutYellow = timeoutYellow;
	}

	public void setTimeoutRed(Duration timeoutRed) {
		this.timeoutRed = timeoutRed;
	}

	public void setHealWhite(Duration healWhite) {
		this.healWhite = healWhite;
	}

	public void setHealYellow(Duration healYellow) {
		this.healYellow = healYellow;
	}

	public void setHealRed(Duration healRed) {
		this.healRed = healRed;
	}

}