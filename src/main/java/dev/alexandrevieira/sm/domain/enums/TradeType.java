package dev.alexandrevieira.sm.domain.enums;

public enum TradeType {
	COMPRA(0, "Compra"),
	VENDA(1, "Venda");
	
	private int cod;
	private String description;
	
	private TradeType(Integer cod, String description) {
		this.cod = cod;
		this.description = description;
	}

	public int getCod() {
		return cod;
	}

	public String getDescription() {
		return description;
	}
	
	public static TradeType toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		
		for (TradeType x : TradeType.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Tipo inválido: " + cod);
	}
}
