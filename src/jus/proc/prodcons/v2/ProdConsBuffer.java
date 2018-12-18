package jus.proc.prodcons.v2;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

	/*
	 * On utilise des sémaphores pour garantir que quand on lit le buffer il n'est
	 * pas vide et que quand on ajoute au buffer il n'est pas plein On ajoute aussi.
	 * deux sémaphore qui s'assure que personne d'autre ne lit ou écrit en même
	 * temps que nous.
	 */

	Message messages[];
	int head;
	int tail;
	int taille;
	Semaphore notFull;
	Semaphore notEmpty;
	Semaphore in;
	Semaphore out;

	public ProdConsBuffer(int n) {
		messages = new Message[n];
		head = 0;
		tail = 0;
		taille = n;
		/*
		 * On initialise les sémaphores :
		 * - in et out servent à s'assurent qu'un seul
		 * thread lit et un seul thread écrit à la fois, ils sont donc initialisés à 1
		 * - le buffer sera plein après n ajout, le sémaphore notFull est initialisé à n
		 * - le buffer est vide au début, le sémaphore notEmpty est initialisé à 0
		 */
		in = new Semaphore(1);
		out = new Semaphore(1);
		notFull = new Semaphore(n);
		notEmpty = new Semaphore(0);
	}

	public boolean estplein() {
		return taille == nmsg();
	}

	public void put(Message m) throws InterruptedException {
		/*
		 * On ajoute un message, on prend donc une des ressources de notFull pour
		 * s'assurer que le buffer n'est pas plein
		 */
		notFull.acquire();
		/* On fait une opération d'écriture on utilise la seule ressource de in */
		in.acquire();
		/* On place le message dans le buffer */
		messages[tail] = m;
		tail = (tail + 1) % taille;
		/* On rend la ressource de in pour que d'autre puissent écrire */
		in.release();
		/* On rend une ressource à notEmpty car on a un message de plus à lire */
		notEmpty.release();
	}

	public Message get() throws InterruptedException {
		/*
		 * On lit un message, on prend un ressource de notEmpty pour s'assurer que le
		 * buffer n'est pas vide
		 */
		notEmpty.acquire();
		/* On fait une opération de lecture on utilise la seule ressource de out */
		out.acquire();
		/* On lit le message */
		Message m = messages[head];
		messages[head] = null;
		head = (head + 1) % taille;
		/* On rend la ressource de out pour que d'autre puissent lire */
		out.release();
		return m;
	}

	public int nmsg() {
		// On compte le nombre de message
		int cmpt = 0;
		for (int i = 0; i < taille; i++) {
			if (messages[i] != null) {
				cmpt++;
			}
		}
		return cmpt;
	}

}