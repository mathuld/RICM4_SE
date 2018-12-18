package jus.proc.prodcons.v1;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class TestProdCons {

	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {

		/* On récupère les informations contenues dans le fichier de paramètres xml */
		Properties properties = new Properties();
		properties
				.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("jus/proc/prodcons/options.xml"));

		int nbP = Integer.parseInt(properties.getProperty("nbP"));
		int nbC = Integer.parseInt(properties.getProperty("nbC"));
		int BufSz = Integer.parseInt(properties.getProperty("BufSz"));
		int ProdTime = Integer.parseInt(properties.getProperty("ProdTime"));
		int ConsTime = Integer.parseInt(properties.getProperty("ConsTime"));
		int Mavg = Integer.parseInt(properties.getProperty("Mavg"));

		/* On crée un buffer de taille adéquate */
		ProdConsBuffer buffer = new ProdConsBuffer(BufSz);

		/* Creation des producteurs */
		Producer producer[] = new Producer[nbP];
		for (int i = 0; i < nbP; i++) {
			producer[i] = new Producer(buffer, Mavg, ProdTime);
		}

		/* Création de consommateurs */
		Consumer consumer[] = new Consumer[nbC];
		for (int i = 0; i < nbC; i++) {
			consumer[i] = new Consumer(buffer, ConsTime);
		}

		int indiceC = 0;
		int indiceP = 0;
		boolean full = false;

		while (!full) {
			/*
			 * On lance au hasard les producteurs et les consommateurs avec une probabilité
			 * de 0.5
			 */
			double rand = Math.random();
			if (rand < 0.5) {
				Thread t = new Thread(producer[indiceP]);
				t.start();
				indiceP++;
			} else {
				Thread t = new Thread(consumer[indiceC]);
				t.start();
				indiceC++;
			}

			/*
			 * Si on a lancé tous les producteurs ou tous les consomateurs, on sort de la
			 * boucle
			 */
			if (indiceC == nbC || indiceP == nbP) {
				full = true;
			}
		}

		/* On lance tous les threads qui n'ont pas été lacé précédement */
		if (indiceC == nbC) {
			for (int i = indiceP; i < nbP; i++) {
				Thread t = new Thread(producer[i]);
				t.start();
			}
		} else if (indiceP == nbP) {
			for (int i = indiceC; i < nbC; i++) {
				Thread t = new Thread(consumer[i]);
				t.start();
			}
		}

	}
}