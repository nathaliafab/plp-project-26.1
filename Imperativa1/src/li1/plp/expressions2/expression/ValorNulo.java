package li1.plp.expressions2.expression;

import li1.plp.expressions1.util.Tipo;
import li1.plp.expressions1.util.TipoPrimitivo;
import li1.plp.expressions2.memory.AmbienteCompilacao;

public class ValorNulo extends ValorConcreto<Object> {
    public ValorNulo() {
        super(null);
    }

    public Tipo getTipo(AmbienteCompilacao amb) {
        return TipoPrimitivo.NULO;
    }

    public ValorNulo clone() {
        return new ValorNulo();
    }
    
    @Override
    public String toString() {
        return "null";
    }
}
