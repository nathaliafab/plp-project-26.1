package li1.plp.expressions1.util;

public class TipoOptional implements Tipo {
    private Tipo baseType;

    public TipoOptional() {
        this.baseType = null;
    }

    public TipoOptional(Tipo baseType) {
        if (baseType instanceof TipoOptional) {
            this.baseType = ((TipoOptional) baseType).getBaseType();
        } else {
            this.baseType = baseType;
        }
    }

    public Tipo getBaseType() {
        return baseType;
    }

    public void setBaseType(Tipo baseType) {
        this.baseType = baseType;
    }

    public String getNome() {
        return "OPTIONAL" + (baseType != null ? "_" + baseType.getNome() : "");
    }

    public boolean eInteiro() {
        return baseType != null && baseType.eInteiro();
    }

    public boolean eBooleano() {
        return baseType != null && baseType.eBooleano();
    }

    public boolean eString() {
        return baseType != null && baseType.eString();
    }

    public boolean eIgual(Tipo tipo) {
        if (tipo.eValido() && tipo.getNome().equals("NULO")) {
            return true;
        }
        if (baseType == null || baseType.getNome().equals("NULO")) {
            return true;
        }
        if (tipo instanceof TipoOptional) {
            TipoOptional tOpt = (TipoOptional) tipo;
            if (tOpt.getBaseType() == null || tOpt.getBaseType().getNome().equals("NULO")) return true;
            return baseType.eIgual(tOpt.getBaseType());
        }
        return baseType.eIgual(tipo);
    }

    public boolean eValido() {
        return true;
    }

    public Tipo intersecao(Tipo outroTipo) {
        return null;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
