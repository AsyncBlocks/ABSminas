package br.asyncblocks.absminas.utilidade;

import org.bukkit.entity.Player;

public class Mina {

    private final String nome;
    private final Cuboid regiao;

    public Mina(String nome,Cuboid regiao) {
        this.nome = nome;
        this.regiao = regiao;
    }

    public Cuboid getRegiao() {
        return regiao;
    }

    public String getNome() {
        return nome;
    }
}
