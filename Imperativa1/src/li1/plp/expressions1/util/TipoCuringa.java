package li1.plp.expressions1.util;

public class TipoCuringa implements Tipo {
    public String getNome() { return "CURINGA"; }
    public boolean eInteiro() { return true; }
    public boolean eBooleano() { return true; }
    public boolean eString() { return true; }
    public boolean eIgual(Tipo tipo) { return true; }
    public boolean eValido() { return true; }
    public Tipo intersecao(Tipo outroTipo) { return outroTipo; }
    @Override public String toString() { return getNome(); }
}
