package jus.proc.prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer {

	/*
	 * Les méthodes du buffer sont synchronized, un seul thread peut se trouver en
	 * même temps dans chaque méthode
	 */

	Message messages[];
	int head;
	int tail;
	int taille;

	public ProdConsBuffer(int n) {
		messages = new Message[n];
		head = 0;
		tail = 0;
		taille = n;
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
		/* On vide la case qui le contenait pour être sur qu'il ne sera pas relu */
		messages[head] = null;
		head = (head + 1) % taille;
		/* On réveille les threads possiblement en attente */
		notifyAll();
		return m;
	}

	public synchronized int nmsg() {
		// On compte le nombre de message
		int cmpt = 0;
		for (int i = 0; i < taille; i++) {
			if (messages[i] != null) {
				cmpt++;
			}
		}
		notifyAll();
		return cmpt;
	}

}
