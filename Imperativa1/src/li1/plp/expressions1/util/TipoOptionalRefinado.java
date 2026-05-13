package li1.plp.expressions1.util;

public class TipoOptionalRefinado extends TipoOptional {
    public TipoOptionalRefinado(Tipo baseType) {
        super(baseType);
    }

    @Override
    public boolean eInteiro() {
        Tipo base = getBaseType();
        return base != null && !base.getNome().equals("NULO") && base.eInteiro();
    }

    @Override
    public boolean eBooleano() {
        Tipo base = getBaseType();
        return base != null && !base.getNome().equals("NULO") && base.eBooleano();
    }

    @Override
    public boolean eString() {
        Tipo base = getBaseType();
        return base != null && !base.getNome().equals("NULO") && base.eString();
    }
}
