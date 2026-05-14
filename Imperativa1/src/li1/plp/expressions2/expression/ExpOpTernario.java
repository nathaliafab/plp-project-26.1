package li1.plp.expressions2.expression;

import li1.plp.expressions1.util.Tipo;
import li1.plp.expressions1.util.TipoOptional;
import li1.plp.expressions1.util.TipoPrimitivo;
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

    private Id getIdChecadoContraNulo(Expressao expressao) {
        if (expressao instanceof ExpEquals) {
            ExpEquals igual = (ExpEquals) expressao;
            if (igual.getEsq() instanceof Id && igual.getDir() instanceof ValorNulo) {
                return (Id) igual.getEsq();
            }
            if (igual.getDir() instanceof Id && igual.getEsq() instanceof ValorNulo) {
                return (Id) igual.getDir();
            }
        }

        if (expressao instanceof ExpNot) {
            return getIdChecadoContraNulo(((ExpNot) expressao).getExp());
        }

        return null;
    }

    private boolean deveRefinarRamoThen(Expressao expressao) {
        return expressao instanceof ExpNot && getIdChecadoContraNulo(((ExpNot) expressao).getExp()) != null;
    }

    private boolean deveRefinarRamoElse(Expressao expressao) {
        return expressao instanceof ExpEquals && getIdChecadoContraNulo(expressao) != null;
    }

    private void refinaIdComoNaoNulo(AmbienteCompilacao amb, Id idToRefine)
            throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        if (idToRefine == null) {
            return;
        }

        Tipo tipo = amb.get(idToRefine);
        if (tipo instanceof TipoOptional) {
            Tipo base = ((TipoOptional) tipo).getBaseType();
            if (base != null && !base.getNome().equals(TipoPrimitivo.NULO.getNome())) {
                amb.map(idToRefine, new TipoOptional(base, true));
            }
        }
    }

    /**
     * Realiza a verificacao de tipos desta expressao.
     */
    @Override
    public boolean checaTipo(AmbienteCompilacao amb)
    throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        if (!getEsq().checaTipo(amb)) {
            return false;
        }

        Tipo condicaoTipo = getEsq().getTipo(amb);
        if (condicaoTipo == null || !condicaoTipo.eBooleano()) {
            return false;
        }

        Id idToRefine = getIdChecadoContraNulo(getEsq());
        boolean refinaThen = deveRefinarRamoThen(getEsq());
        boolean refinaElse = deveRefinarRamoElse(getEsq());

        // Checa e pega tipo para ramo THEN
        amb.incrementa();
        boolean thenResult = false;
        Tipo thenTipo = null;
        try {
            if (refinaThen) {
                refinaIdComoNaoNulo(amb, idToRefine);
            }
            thenResult = getMei().checaTipo(amb);
            thenTipo = getMei().getTipo(amb);
        } finally {
            amb.restaura();
        }

        // Checa e pega tipo para ramo ELSE
        amb.incrementa();
        boolean elseResult = false;
        Tipo elseTipo = null;
        try {
            if (refinaElse) {
                refinaIdComoNaoNulo(amb, idToRefine);
            }
            elseResult = getDir().checaTipo(amb);
            elseTipo = getDir().getTipo(amb);
        } finally {
            amb.restaura();
        }

        if (!thenResult || !elseResult) {
            return false;
        }

        return thenTipo.eIgual(elseTipo)
                || thenTipo.eIgual(TipoPrimitivo.NULO)
                || elseTipo.eIgual(TipoPrimitivo.NULO);
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
    @Override
    public Tipo getTipo(AmbienteCompilacao amb)
    throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Id idToRefine = getIdChecadoContraNulo(getEsq());
        boolean refinaThen = deveRefinarRamoThen(getEsq());
        boolean refinaElse = deveRefinarRamoElse(getEsq());

        // Pega tipo para ramo THEN
        amb.incrementa();
        Tipo thenTipo = null;
        try {
            if (refinaThen) {
                refinaIdComoNaoNulo(amb, idToRefine);
            }
            thenTipo = getMei().getTipo(amb);
        } finally {
            amb.restaura();
        }

        // Pega tipo para ramo ELSE
        amb.incrementa();
        Tipo elseTipo = null;
        try {
            if (refinaElse) {
                refinaIdComoNaoNulo(amb, idToRefine);
            }
            elseTipo = getDir().getTipo(amb);
        } finally {
            amb.restaura();
        }
        
        // Se o ramo 'then' for nulo, o tipo resultante é opcional do tipo do ramo 'else'
        if (thenTipo != null && thenTipo.eIgual(TipoPrimitivo.NULO)) {
            return new TipoOptional(elseTipo);
        }
        
        // Se o ramo 'else' for nulo, o tipo resultante é opcional do tipo do ramo 'then'
        if (elseTipo != null && elseTipo.eIgual(TipoPrimitivo.NULO)) {
            return new TipoOptional(thenTipo);
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