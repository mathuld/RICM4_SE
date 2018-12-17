package jus.proc.prodcons.v3;

public class Consumer implements Runnable {

	ProdConsBuffer buffer;
	int time;
	
	public Consumer (ProdConsBuffer b, int ConsTime) {
		buffer = b;
		time = ConsTime;
		//setDaemon(true);
	}

	@Override
	public void run() {
		
		while(true) {
			ProdConsBuffer.Message m = null;
			int nb = -1;
			try {
				Thread.sleep(time);
				m = buffer.get();
				nb = m.n;
			} catch (InterruptedException e) {}
			
			if(m!=null) {
				System.out.println("[Consumer"+ Thread.currentThread().getId() + "] "+m.s + ", exemplaire : " + nb);
				
				if(m.n==0) {
					buffer.wakeupCP();
				} else {
					buffer.sleepC(this);
				}
			}
		}
	}
}