package com.vcalebef.gerenciamento_estoque_api.exception;

public class StockCapacityException extends RuntimeException {
    public StockCapacityException(String message) {
        super(message);
    }
}
