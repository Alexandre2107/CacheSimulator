
import java.util.Random;

public class Aleatorio extends Mapeamentos {
	public Aleatorio(int tamMemoria, int tamCache, int pal, int l) {
		super(tamMemoria, tamCache, pal, l);
	}

	public static int Substituir(ValoresAssociativo[] posicoes, String valorTag, int hit, int tam) {
		int op = 0;
		int hitCache = hit;
		
		for (int j = 0; j < posicoes.length; j++) {
			if (posicoes[j].getTag().equals(valorTag)) {
				op = 1;
				hitCache++;
				break;
			} else {
				op = 0;
			}
		}
		if (op == 0) {
			Random random = new Random();
			int numeroAleatorio = random.nextInt(tam - 1);

			posicoes[numeroAleatorio].setTag(valorTag);
			op = 0;

		}
		return hitCache;
	}
}
