package jus.proc.prodcons.v1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
		l = new ReentrantLock();
	}

	public boolean estplein(){
		return head==tail;
	}
	
	public void put(Message m) throws InterruptedException {
		while(l.tryLock()&&!estplein()){
			try{wait();}
			catch(InterruptedException e){continue;}
		}
		l.lock();
		messages[tail] = m;
		tail = (tail + 1)%taille;
		l.unlock();
	}
	
	public Message get() throws InterruptedException {
		while(l.tryLock()){
			try{wait();}
			catch(InterruptedException e){continue;}
		}
		l.lock();
		Message m = messages[head];
		head = (head +1)%taille;
		l.unlock();
		return m;
	}
	
	public int nmsg() {
		while(l.tryLock()){
			try{wait();}
			catch(InterruptedException e){continue;}
		}
		l.lock();
		
		l.unlock();
		return taille;
	}

}
