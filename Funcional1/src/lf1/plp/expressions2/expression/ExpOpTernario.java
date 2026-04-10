package lf1.plp.expressions2.expression;

import lf1.plp.expressions1.util.Tipo;
import lf1.plp.expressions2.memory.AmbienteCompilacao;
import lf1.plp.expressions2.memory.AmbienteExecucao;
import lf1.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf1.plp.expressions2.memory.VariavelNaoDeclaradaException;

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

        boolean tiposIguais = thenTipo.eIgual(elseTipo) || thenTipo.eNulo() || elseTipo.eNulo();
        return condicaoTipo.eBooleano() && tiposIguais;
    }

    /**
     * Retorna os tipos possiveis desta expressao.
     */
    public Tipo getTipo(AmbienteCompilacao amb)
    throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Tipo thenTipo = getMei().getTipo(amb);
        Tipo elseTipo = getDir().getTipo(amb);
        
        if ((thenTipo != null && thenTipo.eNulo()) || (elseTipo != null && elseTipo.eNulo())) {
            return lf1.plp.expressions1.util.TipoPrimitivo.NULO;
        }
        
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