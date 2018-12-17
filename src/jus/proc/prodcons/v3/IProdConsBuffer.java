package jus.proc.prodcons.v3;

public interface IProdConsBuffer {
	
	public class Message{
		String s;
		int n;
		public Message (String s,int n) {
			this.s = s;
			this.n = n;
		}
	}

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
