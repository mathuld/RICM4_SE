package jus.proc.prodcons.v1;

public class ProdConsBuffer {
	
	public class Message {
		String s;
	}
	
	public interface IProdConsBuffer {
	
	/**
	* put m in the prodcons buffer
	**/
	public void put(Message m) throws InterruptedException;
	/**
	* retrieve a message from the prodcons buffer, following a fifo order
	**/
	public Message get() throws InterruptedException;
	/**
	* returns the number of messages currently available in the prodcons buffer
	**/
	public int nmsg();
	}
}
