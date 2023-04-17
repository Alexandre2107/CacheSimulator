public class LRU extends Mapeamentos {
	public LRU(int tamMemoria, int tamCache, int pal, int l) {
		super(tamMemoria, tamCache, pal, l);
	}

	public static int Substituir(ValoresAssociativo[] posicoes, String valorTag, int hit) {
		int op = 0;
		int hitCache = hit;
		int menor = 0;
		
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
			for (int j = 0; j < posicoes.length; j++) {
				if (posicoes[j].getAcesso() <= menor) {
					if (posicoes[j].getTag().equals(valorTag)) {
						hitCache++;
						posicoes[j].setAcesso(posicoes[j].getAcesso() + 1);
					} else {
						menor = posicoes[j].getAcesso();
						posicoes[j].setTag(valorTag);
					}
					break;
				}

			}
		}
		return hitCache;
	}

}
