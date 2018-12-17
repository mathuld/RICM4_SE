package jus.proc.prodcons.v2;

import java.util.concurrent.Semaphore;


public class ProdConsBuffer implements IProdConsBuffer{
	
	Message messages[];
	int head;
	int tail;
	int taille;
	Semaphore s;
	
	public ProdConsBuffer(int n) {
		messages = new Message[n];
		head = 0;
		tail = 0;
		taille = n;
		s = new Semaphore(1);
	}

	public boolean estplein(){
		return taille == nmsg();
	}
	
	public synchronized void put(Message m) throws InterruptedException {
		System.out.println("Je produis...");
		while(estplein()){
			wait();
		}
		s.acquire();
		messages[tail] = m;
		tail = (tail + 1)%taille;
		s.release();
	}
	
	public synchronized Message get() throws InterruptedException {
		System.out.println("Je consomme...");
		while(nmsg()==0){
			wait();
		}
		s.acquire();
		Message m = messages[head];
		messages[head] = null;
		head = (head +1)%taille;
		s.release();
		return m;
	}
	
	public int nmsg() {
		//On compte le nombre de message
		
		return (tail - head)%taille;
	}

}
