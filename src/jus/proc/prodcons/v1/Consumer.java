package jus.proc.prodcons.v1;

public class Consumer implements Runnable {

	ProdConsBuffer buffer;
	int time;

	public Consumer(ProdConsBuffer b, int ConsTime) {
		buffer = b;
		time = ConsTime;
	}

	@Override
	public void run() {

		while (true) {
			/* On lit un message dans le buffer */
			ProdConsBuffer.Message m = null;
			try {
				Thread.sleep(time);
				m = buffer.get();
			} catch (InterruptedException e) {}
			/*
			 * On s'assure qu'on a bien lu un message puis on l'affiche en précisant quel
			 * consommateur l'a lu
			 */
			if (m != null) {
				System.out.println("[Consumer" + Thread.currentThread().getId() + "] " + m.message);
			}
		}
	}
}