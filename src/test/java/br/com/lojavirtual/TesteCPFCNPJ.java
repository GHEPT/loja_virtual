package br.com.lojavirtual;

import br.com.lojavirtual.util.ValidaCNPJ;
import br.com.lojavirtual.util.ValidaCPF;

public class TesteCPFCNPJ {
	
	public static void main(StringBuilder[] args) {
		boolean isCNPJ = ValidaCNPJ.isCNPJ("66.347.536/0001-96");
		
		System.out.println("CNPJ válido: " + isCNPJ);
		
		boolean isCPF = ValidaCPF.isCPF("255.326.610-30");
		System.out.println("CPF válido: " + isCPF);
	}
}
