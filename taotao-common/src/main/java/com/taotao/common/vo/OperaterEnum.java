package com.taotao.common.vo;

public enum OperaterEnum {

	EQUAL("="), GREATER(">"), LESS("<"), GREATER_EQUAL(">="), LESS_EQUAL("<="), NOT_EQUAL("<>");

	private String operater;

	private OperaterEnum(String operater) {
		this.operater = operater;
	}

	@Override
	public String toString() {
		return this.operater;
	}
}
