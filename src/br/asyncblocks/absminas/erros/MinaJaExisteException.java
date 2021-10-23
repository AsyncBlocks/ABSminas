package br.asyncblocks.absminas.erros;

public class MinaJaExisteException extends RuntimeException{

    String msg = "Ja Existe uma Mina com os parametros passados!";

    public MinaJaExisteException() {

    }

    @Override
    public String getMessage() {
        return msg;
    }
}
