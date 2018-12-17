package jus.proc.prodcons.v3;

import java.util.LinkedList;

public class ProdConsBuffer implements IProdConsBuffer{
	
	Message messages[];
	int head;
	int tail;
	int taille;
	
	LinkedList<Producer> prod;
	LinkedList<Consumer> cons;
	
	public ProdConsBuffer(int n) {
		messages = new Message[n];
		head = 0;
		tail = 0;
		taille = n;
		prod = new LinkedList<Producer>();
		cons = new LinkedList<Consumer>();
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
		m.n--;
		if(m.n==0) {
			messages[head] = null;
			head = (head +1)%taille;
		}
		
		notifyAll();
		return m;
	}
	
	public synchronized int nmsg() {
		int cmpt = 0;
		for(int i=0; i<taille; i++) {
			if(messages[i] != null) {
				cmpt++;
			}
		}
		notifyAll();
		return cmpt;
	}
	
	public synchronized void sleepP(Producer p) {
		prod.add(p);
		try {
			wait();
		} catch (InterruptedException e) {}
	}
	
	public synchronized void sleepC(Consumer c) {
		cons.add(c);
		try {
			wait();
		} catch (InterruptedException e) {}
	}
	
	public synchronized void wakeupCP() {
		if(prod.size()>0) {
			Producer p = prod.remove();
			synchronized(p) {
				p.notify();
			}
		}

		while(cons.size()>0) {
			Consumer c = cons.remove();
			synchronized(c) {
				c.notify();
			}
		}
	}
	

}
