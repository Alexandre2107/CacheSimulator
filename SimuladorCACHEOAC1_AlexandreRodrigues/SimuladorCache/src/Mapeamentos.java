
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Mapeamentos {
	protected int tamanhoCache;
	protected static int tamanhoMemoria;
	protected static int palavra;
	protected static int nLinhas;
	protected static int logLinha;
	protected int count = 0;
	protected float missCache = 0;
	protected int hitCache = 0;


	public String getInfo() {
		String x = Integer.toString(count);
		String y = Float.toString(missCache);
		String z = Integer.toString(hitCache);
		String a = Float.toString((hitCache * 100) / count);

		return "Número de Acessos: " + x + '\n' + "MissCache: " + y + '\n' + "HitCache: " + z  + '\n' + "HitRate: " + a + " %";
	}

	public Mapeamentos(int tamMemoria, int tamCache, int pal, int l) {
		this.tamanhoMemoria = tamMemoria;
		this.tamanhoCache = tamCache;
		this.palavra = pal;
		this.nLinhas = l;
		this.logLinha = (int) (Math.log(nLinhas) / Math.log(2));
	}

	public void Direto() {
		// Calculo de quantos bits a tag tem
		int tag = (int) (Math.log(tamanhoMemoria / palavra) / Math.log(2)) + 1 - logLinha - 2;

		String[] posicoes = new String[nLinhas];
		String valorTag;
		String valorLinha;
		int endereco = 0;
		int lCache; 
		count = 0;
		missCache = 0;
		hitCache = 0; 

		ArrayList<String> teste = FileManager.stringReader("D:/SimuladorCacheOAC/SimuladorCache_AlexandreRodrigues/data/teste_1.txt");
		for (String linha : teste) {
			int acesso = Integer.parseInt(linha);

			endereco = tag + logLinha + 2;

			String stringBin = intToBinaryString(acesso, endereco);

			valorTag = stringBin.substring(0, tag);
			valorLinha = stringBin.substring(tag, logLinha + tag);

			// System.out.println(valorTag);
			// System.out.println(valorLinha);

			lCache = Integer.parseInt(valorLinha, 2);

			if (posicoes[lCache] == null) {
				posicoes[lCache] = valorTag;
				missCache++;
			} else if (posicoes[lCache].equals(valorTag)) {
				hitCache++;
			} else {
				posicoes[lCache] = valorTag;
				missCache++;
			}
			count++;
		}
		mostrarInformacoes(count, hitCache, missCache);
	}


	public void Associativo(String opcao) {
		int tag = (int) (Math.log(tamanhoMemoria / palavra) / Math.log(2)) - 2;

		ValoresAssociativo[] posicoes = new ValoresAssociativo[nLinhas];
		String valorTag;
		int i = 0, endereco = 0, valor = 0, id = 0;
		count = 0;
		hitCache = 0;
		missCache = 0;

		ArrayList<String> teste = FileManager.stringReader("D:/SimuladorCacheOAC/SimuladorCache_AlexandreRodrigues/data/teste_1.txt");
		for (String linha : teste) {
			int acesso = Integer.parseInt(linha);

			endereco = tag + 2;

			// int bin[] = intToBinary(acesso, endereco);

			String stringBin = intToBinaryString(acesso, endereco);

			valorTag = stringBin.substring(0, tag);
			ValoresAssociativo end = new ValoresAssociativo(valorTag, valor);
			if (i < nLinhas) {
				posicoes[i] = end;
			} else {
				if (opcao == "LRU") {
					hitCache = LRU.Substituir(posicoes, valorTag, hitCache);
				} else if (opcao == "FIFO") {
					hitCache = FIFO.Substituir(posicoes, valorTag, hitCache, id);
					id++;
				} else if (opcao == "LFU") {
					hitCache = LFU.Substituir(posicoes, valorTag, end, hitCache);
				} else if (opcao == "aleatorio") {
					hitCache = Aleatorio.Substituir(posicoes, valorTag, hitCache, nLinhas);
				}
			}
			count++;
			i++;
		}
		missCache = (int) (count - hitCache);
		mostrarInformacoes(count, hitCache, missCache);
	}

	public void AssociativoConjunto(String opcao, int tam) {
		int endereco = 0;
		int i = 0, j = 0, nBloco = 0, blocos = 1, tag = 0, logBlocos = 0, valor = 0, id = 0;
		count = 0;
		missCache = 0;
		hitCache = 0;
		blocos = tam / nLinhas;
		tag = (int) ((int) (Math.log(tamanhoMemoria / palavra) / Math.log(2)) + 1 - (Math.log(blocos) / Math.log(2))
		- 2);
		logBlocos = (int) (Math.log(blocos) / Math.log(2));

		ValoresAssociativo[][] posicoes = new ValoresAssociativo[blocos][tam];
		String valorTag;
		String valorBloco;

		ArrayList<String> teste = FileManager.stringReader("D:/SimuladorCacheOAC/SimuladorCache_AlexandreRodrigues/data/teste_1.txt");
		for (String linha : teste) {
			int acesso = Integer.parseInt(linha);

			endereco = tag + logBlocos + 2;
			// int bin[] = intToBinary(acesso, endereco);

			String stringBin = intToBinaryString(acesso, endereco);

			valorTag = stringBin.substring(0, tag);
			valorBloco = stringBin.substring(tag, logBlocos + tag);
			nBloco = Integer.parseInt(valorBloco, 2);

			ValoresAssociativo end = new ValoresAssociativo(valorTag, valor);
			for (j = 0; j < tam; j++) {
				if (posicoes[nBloco][j] == null) {
					posicoes[nBloco][j] = end;
				}else {
					i++;
				}
			}
			if (i != 0) {
				if (opcao == "LRU") {
					hitCache = LRU.Substituir(posicoes[nBloco], valorTag, hitCache);
				} else if (opcao == "FIFO") {
					hitCache = FIFO.Substituir(posicoes[nBloco], valorTag, hitCache, id);
					id++;
				} else if (opcao == "LFU") {
					hitCache = LFU.Substituir(posicoes[nBloco], valorTag, end, hitCache);
				} else if (opcao == "aleatorio") {
					hitCache = Aleatorio.Substituir(posicoes[nBloco], valorTag, hitCache, tam);
				}
			}

			count++;
		}

		missCache = (int) (count - hitCache);
		mostrarInformacoes(count, hitCache, missCache);
	}

	public void mostrarInformacoes(int count, int hitCache, float missCache) {
		System.out.println("Acessos: " + count);
		System.out.println("MissCache: " + missCache);
		System.out.println("HitCache: " + hitCache);
		System.out.println("Hit Rate: " +  (hitCache * 100) / count + "%");
	}

	public static int[] intToBinary(int value, int size) {
		if (value > Math.pow(2, size) - 1) {
			return null;
		}
		int bin[] = new int[size];
		int i = 0;
		while (value > 0 && i < size) {
			int num = value % 2;
			value = value / 2;
			bin[i] = num;
			i++;
		}
		for (int j = 0; j <= size / 2; j++) {
			int temp = bin[j];
			bin[j] = bin[size - j - 1];
			bin[size - j - 1] = temp;
		}
		return bin;
	}

	public static String intToBinaryString(int value, int size) {
		if (value > Math.pow(2, size) - 1) {
			return null;
		}
		char bin[] = new char[size];
		for (int i = 0; i < size; i++) {
			bin[i] = '0';
		}
		int i = 0;
		while (value > 0 && i < size) {
			int num = value % 2;
			value = value / 2;
			bin[i] = (num + "").charAt(0);
			i++;
		}
		for (int j = 0; j <= size / 2; j++) {
			char temp = bin[j];
			bin[j] = bin[size - j - 1];
			bin[size - j - 1] = temp;
		}
		String nova = new String(bin);
		return nova;
	}
}
