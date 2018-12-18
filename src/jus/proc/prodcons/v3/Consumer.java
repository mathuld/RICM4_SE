package jus.proc.prodcons.v3;

public class Consumer implements Runnable {

	ProdConsBuffer buffer;
	int time;

	public Consumer(ProdConsBuffer b, int ConsTime) {
		buffer = b;
		time = ConsTime;
		// setDaemon(true);
	}

	@Override
	public void run() {

		while (true) {
			/* On lit un message dans le buffer */
			ProdConsBuffer.Message m = null;
			int nb = -1;
			try {
				Thread.sleep(time);
				m = buffer.get();
				nb = m.exemplaire;
			} catch (InterruptedException e) {
			}
			/*
			 * On s'assure qu'on a bien lu un message puis on l'affiche en précisant quel
			 * consommateur l'a lu
			 */
			if (m != null) {

				System.out.println(
						"[Consumer" + Thread.currentThread().getId() + "] " + m.message + ", exemplaire : " + nb);
				/*
				 * S'il s'agit du dernier exemplaire, on appelle la fonction qui réveillera les
				 * bon threads, sinon on appelle la fonction qui endors le thread
				 */
				if (nb == 0) {
					buffer.wakeupCP();
				} else {
					buffer.sleepC(this);
				}
			}
		}
	}
}