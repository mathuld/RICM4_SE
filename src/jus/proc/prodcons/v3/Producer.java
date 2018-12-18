package jus.proc.prodcons.v3;

public class Producer implements Runnable {

	ProdConsBuffer buffer;
	int avg;
	int time;
	int n;

	public Producer(ProdConsBuffer b, int Mavg, int ProdTime, int n) {
		buffer = b;
		avg = Mavg;
		time = ProdTime;
		this.n = n;
	}

	@Override
	public void run() {
		/* On tire au hasard le nombre de message produit par un producteur */
		int nbmsg = (int) (Math.random() * avg * 2);

		System.out.println("Le prod " + Thread.currentThread().getId() + " produit " + nbmsg);

		for (int i = 0; i < nbmsg; i++) {
			/*
			 * Un message doit contenir l'identifiant du producteur et le numéro du message
			 */
			ProdConsBuffer.Message m = new ProdConsBuffer.Message(
					"Producer : " + Thread.currentThread().getId() + " Message : " + i, n);
			try {
				Thread.sleep(time);
				buffer.put(m);
				/* Après avoir placé son message dans le buffer, le producteur s'endort */
				buffer.sleepP(this);
			} catch (InterruptedException e) {}
		}
	}
}
