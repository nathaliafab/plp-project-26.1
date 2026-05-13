package li1.plp.expressions1.util;

public class TipoOptional implements Tipo {
    private Tipo baseType;
    // Quando isRefinado é false, o operações em cima dele lançam erro de tipo
    // Quando isRefinado é true, o tipo é considerado refinado e as operações são permitidas caso válidas
    // isRefinado é true quando o null foi checado por um condicional
    private boolean isRefinado;

    public TipoOptional() {
        this.baseType = null;
        this.isRefinado = false;
    }

    public TipoOptional(Tipo baseType) {
        if (baseType instanceof TipoOptional) {
            this.baseType = ((TipoOptional) baseType).getBaseType();
            this.isRefinado = ((TipoOptional) baseType).isRefinado();
        } else {
            this.baseType = baseType;
            this.isRefinado = false;
        }
    }

    public TipoOptional(Tipo baseType, boolean isRefinado) {
        if (baseType instanceof TipoOptional) {
            this.baseType = ((TipoOptional) baseType).getBaseType();
        } else {
            this.baseType = baseType;
        }
        this.isRefinado = isRefinado;
    }

    public boolean isRefinado() {
        return this.isRefinado;
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
        return isRefinado && baseType != null && !baseType.getNome().equals("NULO") && baseType.eInteiro();
    }

    public boolean eBooleano() {
        return isRefinado && baseType != null && !baseType.getNome().equals("NULO") && baseType.eBooleano();
    }

    public boolean eString() {
        return isRefinado && baseType != null && !baseType.getNome().equals("NULO") && baseType.eString();
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
        if (this.eIgual(outroTipo)) {
            return this;
        }
        return null;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
