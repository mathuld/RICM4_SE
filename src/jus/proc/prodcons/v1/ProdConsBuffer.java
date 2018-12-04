package jus.proc.prodcons.v1;

import java.util.concurrent.locks.Lock;

public class ProdConsBuffer implements IProdConsBuffer{
	
	Message messages[];
	int head;
	int tail;
	int taille;
	Lock l;
	
	public ProdConsBuffer(int n) {
		messages = new Message[n];
		head = 0;
		tail = 0;
		taille = n;
		l = new Lock();
	}
	
	public void put(Message m) throws InterruptedException {
		l.lock();
	
		
		l.unlock();
	}
	
	public Message get() throws InterruptedException {
		l.lock();


		l.unlock();
		return null;
	}
	
	public int nmsg() {
		l.lock();
		
		l.unlock();
		return taille;
	}

}
