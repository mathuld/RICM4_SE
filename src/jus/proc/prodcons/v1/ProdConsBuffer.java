package jus.proc.prodcons.v1;


public class ProdConsBuffer implements IProdConsBuffer{
	
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

	public boolean estplein(){
		return taille == nmsg();
	}
	
	public synchronized void put(Message m) throws InterruptedException {
		while(estplein()){
			wait();
		}
		messages[tail] = m;
		tail = (tail + 1)%taille;
		notifyAll();
	}
	
	public synchronized Message get() throws InterruptedException {
		while(nmsg()==0){
			wait();
		}
		Message m = messages[head];
		messages[head] = null;
		head = (head +1)%taille;
		notifyAll();
		return m;
	}
	
	public synchronized int nmsg() {
		//On compte le nombre de message
		int cmpt = 0;
		for(int i=0; i<taille; i++) {
			if(messages[i] != null) {
				cmpt++;
			}
		}
		notifyAll();
		return cmpt;
	}

}
