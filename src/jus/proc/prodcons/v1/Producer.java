package jus.proc.prodcons.v1;

public class Producer implements Runnable {

	ProdConsBuffer buffer;
	int avg;
	int time;
	
	public Producer (ProdConsBuffer b, int Mavg, int ProdTime) {
		buffer = b;
		avg = Mavg;
		time = ProdTime;
	}

	@Override
	public void run() {
		
		int nbmsg = (int) (Math.random() * avg * 2);
		
		System.out.println("Le prod " + Thread.currentThread().getId()+" produit "+ nbmsg);
		
		for(int i=0; i<nbmsg; i++) {
			ProdConsBuffer.Message m = new ProdConsBuffer.Message("Producer : " + Thread.currentThread().getId() + " Message : " + i);
			try {
				Thread.sleep(time);
				buffer.put(m);
			} catch (InterruptedException e) {}
		}
	}
}
