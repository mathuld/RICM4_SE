package jus.proc.prodcons.v3;

public interface IProdConsBuffer {

	/* On modifie le classe Message afin d'avoir un nombre d'exemplaire */
	public class Message {
		String message;
		int exemplaire;

		public Message(String s, int n) {
			this.message = s;
			this.exemplaire = n;
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
