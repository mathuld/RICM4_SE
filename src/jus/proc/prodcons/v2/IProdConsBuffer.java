package jus.proc.prodcons.v2;

public interface IProdConsBuffer {

	/* Les messages qu'on produit et consomme sont des chaînes de caractères */
	public class Message {
		String message;

		public Message(String s) {
			this.message = s;
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
