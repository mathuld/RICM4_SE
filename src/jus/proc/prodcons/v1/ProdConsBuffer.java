package jus.proc.prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer{
	
	Message message[];
	int head;
	int tail;
	int taille;
	
	public ProdConsBuffer(int n) {
		message = new Message[n];
		head = 0;
		tail = 0;
		taille = n;
	}
	
	public void put(Message m) throws InterruptedException {
		
		
		
		
	}
	
	public Message get() throws InterruptedException {
		return null;
		
	}
	
	public int nmsg() {
		return 0;
		
	}

}
