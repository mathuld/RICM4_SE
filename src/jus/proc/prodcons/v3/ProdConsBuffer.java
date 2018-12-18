package jus.proc.prodcons.v3;

import java.util.LinkedList;

public class ProdConsBuffer implements IProdConsBuffer {

	/*
	 * Les méthodes du buffer sont synchronized, un seul thread peut se trouver en
	 * même temps dans chaque méthode
	 */
	Message messages[];
	int head;
	int tail;
	int taille;
	/*
	 * On ajoute deux listes chaînées pour stocké les consommateurs et les
	 * producteurs bloqués
	 */
	LinkedList<Producer> prod;
	LinkedList<Consumer> cons;

	public ProdConsBuffer(int n) {
		messages = new Message[n];
		head = 0;
		tail = 0;
		taille = n;
		prod = new LinkedList<Producer>();
		cons = new LinkedList<Consumer>();
	}

	public boolean estplein() {
		return taille == nmsg();
	}

	public synchronized void put(Message m) throws InterruptedException {
		/*
		 * Pour pouvoir mettre un message dans le buffer, il faut que le buffer ne soit
		 * pas plein
		 */
		while (estplein()) {
			/* Si le buffer est plein, le thread attend */
			wait();
		}
		/* On ajoute le message à la fin du buffer */
		messages[tail] = m;
		tail = (tail + 1) % taille;
		/* On réveille les threads possiblement en attente mis en attente */
		notifyAll();
	}

	public synchronized Message get() throws InterruptedException {
		/*
		 * Pour pouvoir lire un message, il faut qu'il y ait au moins un message dans le
		 * buffer
		 */
		while (nmsg() == 0) {
			/* Si le buffer est vide, le thread attend */
			wait();
		}
		/* On récupère le message au début du buffer */
		Message m = messages[head];
		/* On décrèmente le nombre d'exemplaire à lire du message */
		m.exemplaire--;
		/* S'il n'y a plus d'exemplaire à lire on retire le messsage du buffer */
		if (m.exemplaire == 0) {
			messages[head] = null;
			head = (head + 1) % taille;
		}
		/* On réveille les threads possiblement en attente */
		notifyAll();
		return m;
	}

	public synchronized int nmsg() {
		/* On compte le nombre de messages du buffer */
		int cmpt = 0;
		for (int i = 0; i < taille; i++) {
			if (messages[i] != null) {
				cmpt++;
			}
		}
		notifyAll();
		return cmpt;
	}

	public synchronized void sleepP(Producer p) {
		/*
		 * On appelle cette fonction avec un producteur pour le placer dans la file
		 * d'attente des bloqués et le mettre en attente
		 */
		prod.add(p);
		try {
			wait();
		} catch (InterruptedException e) {
		}
	}

	public synchronized void sleepC(Consumer c) {
		/*
		 * On appelle cette fonction avec un consommateur pour le placer dans la file
		 * d'attente des bloqués et le mettre en attente
		 */
		cons.add(c);
		try {
			wait();
		} catch (InterruptedException e) {
		}
	}

	public synchronized void wakeupCP() {
		/*
		 * On entre dans cette fonction quand tous les exemplaires d'un message ont été
		 * lu
		 *
		 * Le premier élément de le liste de producteurs bloqués est le producteur du
		 * message qu'on vient de finir donc on le réveille
		 */
		if (prod.size() > 0) {
			Producer p = prod.remove();
			synchronized (p) {
				p.notify();
			}
		}

		/*
		 * Tous les consommateurs bloqués sont les consommateurs qui ont lu les autres
		 * exemplaires du messages donc on les bloque
		 */
		while (cons.size() > 0) {
			Consumer c = cons.remove();
			synchronized (c) {
				c.notify();
			}
		}
	}

}
