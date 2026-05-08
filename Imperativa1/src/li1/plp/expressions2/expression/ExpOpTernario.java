package li1.plp.expressions2.expression;

import li1.plp.expressions1.util.Tipo;
import li1.plp.expressions2.memory.AmbienteCompilacao;
import li1.plp.expressions2.memory.AmbienteExecucao;
import li1.plp.expressions2.memory.VariavelJaDeclaradaException;
import li1.plp.expressions2.memory.VariavelNaoDeclaradaException;

public class ExpOpTernario extends ExpTernaria {

    public ExpOpTernario(Expressao teste, Expressao thenExpressao,
        Expressao elseExpressao) {
        super(teste, thenExpressao, elseExpressao, "?", ":");
    }

    public Valor avaliar(AmbienteExecucao ambiente)
    throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        if (((ValorBooleano) getEsq().avaliar(ambiente)).valor())
            return getMei().avaliar(ambiente);
        else
            return getDir().avaliar(ambiente);
    }

    /**
     * Realiza a verificacao de tipos desta expressao.
     */
    @Override
    protected boolean checaTipoElementoTerminal(AmbienteCompilacao amb)
    throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Tipo condicaoTipo = getEsq().getTipo(amb);
        Tipo thenTipo = getMei().getTipo(amb);
        Tipo elseTipo = getDir().getTipo(amb);

        boolean tiposIguais = thenTipo.eIgual(elseTipo) || thenTipo.eIgual(li1.plp.expressions1.util.TipoPrimitivo.NULO) || elseTipo.eIgual(li1.plp.expressions1.util.TipoPrimitivo.NULO);
        return condicaoTipo.eBooleano() && tiposIguais;
    }

    /**
     * Retorna os tipos possiveis desta expressao.
     */
    public Tipo getTipo(AmbienteCompilacao amb)
    throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Tipo thenTipo = getMei().getTipo(amb);
        Tipo elseTipo = getDir().getTipo(amb);
        
        // Se o ramo 'then' for nulo, o tipo resultante é opcional do tipo do ramo 'else'
        if (thenTipo != null && thenTipo.eIgual(li1.plp.expressions1.util.TipoPrimitivo.NULO)) {
            return new li1.plp.expressions1.util.TipoOptional(elseTipo);
        }
        
        // Se o ramo 'else' for nulo, o tipo resultante é opcional do tipo do ramo 'then'
        if (elseTipo != null && elseTipo.eIgual(li1.plp.expressions1.util.TipoPrimitivo.NULO)) {
            return new li1.plp.expressions1.util.TipoOptional(thenTipo);
        }
        
        // Se nenhum for nulo, segue a lógica normal de interseção
        return thenTipo.intersecao(elseTipo);
    }

    @Override
    public String toString() {
        return String.format("(%s) ? (%s) : (%s)", getEsq(), getMei(),
            getDir());
    }

    public ExpOpTernario clone() {
        return new ExpOpTernario(getEsq().clone(), getMei().clone(), getDir().clone());
    }
}