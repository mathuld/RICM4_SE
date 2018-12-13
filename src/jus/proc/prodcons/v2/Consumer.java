package jus.proc.prodcons.v2;

public class Consumer implements Runnable {

	ProdConsBuffer buffer;
	int time;
	
	public Consumer (ProdConsBuffer b, int ConsTime) {
		buffer = b;
		time = ConsTime;
	}

	@Override
	public void run() {
		
		while(true) {
			ProdConsBuffer.Message m = null;
			try {
				Thread.sleep(time);
				m = buffer.get();
			} catch (InterruptedException e) {}
			
			if(m!=null) {
				System.out.println("[Consumer"+ Thread.currentThread().getId() + "] "+m.s);
			}
		}
	}
}