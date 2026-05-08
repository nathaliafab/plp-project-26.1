package li1.plp.expressions2.expression;

import li1.plp.expressions1.util.Tipo;
import li1.plp.expressions1.util.TipoPrimitivo;
import li1.plp.expressions1.util.TipoOptional;
import li1.plp.expressions2.memory.AmbienteCompilacao;
import li1.plp.expressions2.memory.AmbienteExecucao;
import li1.plp.expressions2.memory.VariavelJaDeclaradaException;
import li1.plp.expressions2.memory.VariavelNaoDeclaradaException;

public class ExpNullCoalescing extends ExpBinaria {

    public ExpNullCoalescing(Expressao esq, Expressao dir) {
        super(esq, dir, "??");
    }

    @Override
    public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Valor valorEsq = getEsq().avaliar(amb);

		// Se o valor da expressão da esquerda for nulo, retorna o valor da expressão da direita
        if (valorEsq instanceof ValorNulo) {
            return getDir().avaliar(amb);
        }
        return valorEsq;
    }

    @Override
    protected boolean checaTipoElementoTerminal(AmbienteCompilacao ambiente)
            throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Tipo tipoEsq = getEsq().getTipo(ambiente);
        Tipo tipoDir = getDir().getTipo(ambiente);

		// Se a expressão da esquerda for do tipo nulo, a expressão da direita deve ser válida
        if (tipoEsq.getNome().equals("NULO")) {
            return tipoDir.eValido();
        }

        if (tipoEsq instanceof TipoOptional) {
            Tipo base = ((TipoOptional) tipoEsq).getBaseType();
            
			// Se a base do tipo opcional for nula, o resultado é válido se a expressão da direita for válida
            if (base == null || base.getNome().equals("NULO")) {
                return tipoDir.eValido();
            }
            
			// Se a base do tipo opcional for diferente de nulo, o resultado é válido se a base for igual ao tipo da expressão da direita
            return base.getNome().equals(tipoDir.getNome());
        }
        
		// Se a expressão da esquerda não for nula nem opcional, os tipos devem ser iguais
        return tipoEsq.eIgual(tipoDir);
    }

    @Override
    public Tipo getTipo(AmbienteCompilacao ambiente) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
        Tipo tipoEsq = getEsq().getTipo(ambiente);
        Tipo tipoDir = getDir().getTipo(ambiente);

        if (tipoEsq instanceof TipoOptional) {
            Tipo base = ((TipoOptional) tipoEsq).getBaseType();
            if (base != null && !base.getNome().equals("NULO")) {
                return base;
            }
        }
        
		// Se a expressão da esquerda for do tipo nulo, o tipo resultante é o tipo da expressão da direita
        return tipoDir;
    }

    @Override
    public ExpBinaria clone() {
        return new ExpNullCoalescing(getEsq().clone(), getDir().clone());
    }
}