package jus.proc.prodcons.v3;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class TestProdCons {

	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {

		Properties properties = new Properties();
		properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("jus/proc/prodcons/options.xml"));

		int nbP = Integer.parseInt(properties.getProperty("nbP"));
		int nbC = Integer.parseInt(properties.getProperty("nbC"));
		int BufSz = Integer.parseInt(properties.getProperty("BufSz"));
		int ProdTime = Integer.parseInt(properties.getProperty("ProdTime"));
		int ConsTime = Integer.parseInt(properties.getProperty("ConsTime"));
		int Mavg = Integer.parseInt(properties.getProperty("Mavg"));
		int n = 3;
		
		ProdConsBuffer buffer = new ProdConsBuffer(BufSz);
		
		//Creation des producteurs
		Producer producer[] = new Producer[nbP];
		for(int i=0; i<nbP; i++) {
			producer[i] = new Producer(buffer, Mavg, ProdTime,n);
		}
		
		//Création de consommateurs
		Consumer consumer[] = new Consumer[nbC];
		for(int i=0; i<nbC; i++) {
			consumer[i] = new Consumer(buffer, ConsTime);
		}

		int indiceC = 0;
		int indiceP = 0;
		boolean full = false;
		
		while(!full) {
			double rand = Math.random();
			if(rand<0.5) {
				Thread t = new Thread(producer[indiceP]);
				t.start();
				indiceP++;
			}
			else {
				Thread t = new Thread(consumer[indiceC]);
				t.start();
				indiceC++;
			}
			
			if(indiceC == nbC || indiceP == nbP) {
				full = true;
			}
		}	
		
		if(indiceC == nbC) {
			for(int i=indiceP; i<nbP; i++) {
				Thread t = new Thread(producer[i]);
				t.start();
			}
		}else if(indiceP == nbP) {
			for(int i=indiceC; i<nbC; i++) {
				Thread t = new Thread(consumer[i]);
				t.start();
			}
		}
		
		
	}
}