package jus.proc.prodcons.v2;

import java.util.concurrent.Semaphore;


public class ProdConsBuffer implements IProdConsBuffer{
	
	Message messages[];
	int head;
	int tail;
	int taille;
	Semaphore notFull;
	Semaphore notEmpty;
	Semaphore in;
	Semaphore out;
	
	public ProdConsBuffer(int n) {
		messages = new Message[n];
		head = 0;
		tail = 0;
		taille = n;
		in = new Semaphore(1);
		out = new Semaphore(1);
		notFull = new Semaphore(n);
		notEmpty = new Semaphore(0);
	}

	public boolean estplein(){
		return taille == nmsg();
	}
	
	public void put(Message m) throws InterruptedException {
		System.out.println("Je suis " + Thread.currentThread().getId()+ " et je produis");
		notFull.acquire();
		in.acquire();
		messages[tail] = m;
		tail = (tail + 1)%taille;
		in.release();
		notEmpty.release();
	}
	
	public Message get() throws InterruptedException {
		System.out.println("Je suis " + Thread.currentThread().getId()+ " et je consomme");
		notEmpty.acquire();
		out.acquire();
		Message m = messages[head];
		messages[head] = null;
		head = (head +1)%taille;
		out.release();
		return m;
	}
	
	public int nmsg() {
		//On compte le nombre de message
		return (tail - head)%taille +1;
	}

}