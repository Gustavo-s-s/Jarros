package br.com.application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Jug VectorJug[][] = asking();
		if (verifyEquals(VectorJug)) 
			System.out.println("O resultado e igual o inicial. NADA À FAZER :(");
		else if (verifySum(VectorJug)) 
			System.out.println("A soma de litros desejada deve ser igual a soma de litros inicial");
		//else if (verify(VectorJug[0]))
			//System.out.println("Capacidade de litros maior que a quantidade. Irá entrar em loop!!!");
		else{
			JugTree tree = new JugTree(VectorJug[0], VectorJug[1]);
			long timeInitial = System.currentTimeMillis();
			tree.spread();
			long timeFinal = System.currentTimeMillis();
			System.out.println("Resolução passo a passo. Tempo Execução : " + (timeFinal - timeInitial));
			System.out.println(tree.log());
		}
		
	}
	
	private static boolean verifySum(Jug[][] vectorJug) {
		int sum = Arrays.asList(vectorJug[0]).stream().mapToInt(Jug::getLiters).sum();
		int sum1 = Arrays.asList(vectorJug[1]).stream().mapToInt(Jug::getLiters).sum();
		return sum != sum1;
	}

	private static boolean verifyEquals(Jug[][] vectorJug) {
		return Arrays.equals(vectorJug[0], vectorJug[1]);
	}

	private static boolean verify(Jug[] vectorJug){
		boolean result = false;
		for(int i =0; i < vectorJug.length; i++){
			for(int j = 0; j < vectorJug.length; j++){
				if (i!=j){
					int capacityI = vectorJug[i].getCapacity();
					int capacityJ = vectorJug[j].getCapacity();
					int sumLiters = vectorJug[i].getLiters() + vectorJug[j].getLiters();
					if(sumLiters < capacityJ && sumLiters < capacityI){
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	public static Jug[][] asking () {
		final Scanner INT_SCAN = new Scanner(System.in);
		final String askingFor[] = {"1 - a capacidade do", "2 - a quantidade de litros do", "3 - o valor desejado de litros no"};
		
		final int answer[] = new int[askingFor.length]; 
		final Jug jugVector[][] = new Jug[2][3];
		
		for (int i = 0; i < answer.length; i++) {
			for (int j = 0; j < answer.length; j++) {
				System.out.printf("Qual %s jarro %d%n", askingFor[j], i+1);
				try {
					answer[j] = INT_SCAN.nextInt();
				} catch (InputMismatchException e) {
					String msg = "ERRO! Entrada inválida";
					System.out.println("=".repeat(msg.length()));
					System.out.println(msg);
					System.out.println("Informe os dados de novo!");
					System.out.println("=".repeat(msg.length()));
					return asking();
				}
			}
			jugVector[0][i] = new Jug(answer[0], answer[1]); 
			jugVector[1][i] = new Jug(answer[0], answer[2]); 
		}
		
		return jugVector;
	}

}
