package li1.plp.expressions2.expression;

import li1.plp.expressions1.util.Tipo;
import li1.plp.expressions1.util.TipoOptional;
import li1.plp.expressions2.memory.AmbienteCompilacao;
import li1.plp.expressions2.memory.AmbienteExecucao;
import li1.plp.expressions2.memory.VariavelJaDeclaradaException;
import li1.plp.expressions2.memory.VariavelNaoDeclaradaException;

/**
 * Um objeto desta classe representa uma Expressao de Assercao Nula (!).
 */
public class ExpNullAssertion extends ExpUnaria {

    public ExpNullAssertion(Expressao exp) {
        super(exp, "!");
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelJaDeclaradaException, VariavelNaoDeclaradaException {
        Valor valor = getExp().avaliar(amb);
        if (valor instanceof ValorNulo) {
            throw new RuntimeException("Erro de execucao: valor nulo encontrado na assercao (!).");
        }
        return valor;
    }

    @Override
    protected boolean checaTipoElementoTerminal(AmbienteCompilacao amb) throws VariavelJaDeclaradaException, VariavelNaoDeclaradaException {
        return true;
    }
    
    @Override
    public Tipo getTipo(AmbienteCompilacao amb) throws VariavelJaDeclaradaException, VariavelNaoDeclaradaException {
        Tipo tipo = getExp().getTipo(amb);
        if (tipo instanceof TipoOptional) {
            Tipo baseType = ((TipoOptional) tipo).getBaseType();
            if (baseType == null || baseType.getNome().equals("NULO")) {
                return new li1.plp.expressions1.util.TipoCuringa();
            }
            return baseType;
        }
        if (tipo != null && tipo.getNome().equals("NULO")) {
            return new li1.plp.expressions1.util.TipoCuringa();
        }
        return tipo;
    }

    @Override
    public ExpUnaria clone() {
        return new ExpNullAssertion(exp.clone());
    }

    @Override
    public String toString() {
        return String.format("%s!", exp);
    }
}
