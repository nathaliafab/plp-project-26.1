package lf1.plp.expressions2.expression;

import lf1.plp.expressions1.util.Tipo;
import lf1.plp.expressions1.util.TipoPrimitivo;
import lf1.plp.expressions2.memory.AmbienteCompilacao;
import lf1.plp.expressions2.memory.AmbienteExecucao;
import lf1.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf1.plp.expressions2.memory.VariavelNaoDeclaradaException;

/**
 * Um objeto desta classe representa uma Expressao de Null Coalescing.
 */
public class ExpNullCoalescing extends ExpBinaria {

	/**
	 * Constrói uma expressão de Null Coalescing com as sub-expressões especificadas.
	 *
	 * @param esq Expressao da esquerda (que pode ser opcionalmente nula)
	 * @param dir Expressao da direita (valor default)
	 */
	public ExpNullCoalescing(Expressao esq, Expressao dir) {
		super(esq, dir, "??");
	}

	/**
	 * Retorna o valor da Expressao de Null Coalescing
	 */
	@Override
	public Valor avaliar(AmbienteExecucao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		Valor valorEsq = getEsq().avaliar(amb);
		if (valorEsq instanceof ValorNulo) {
			return getDir().avaliar(amb);
		}
		return valorEsq;
	}

	/**
	 * Realiza a verificacao de tipos desta expressao.
	 */
	@Override
	protected boolean checaTipoElementoTerminal(AmbienteCompilacao ambiente)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		Tipo tipoEsq = getEsq().getTipo(ambiente);
		Tipo tipoDir = getDir().getTipo(ambiente);

		// O lado esquerdo pode ser qualquer coisa que potencialmente pode ser null. 
		// Se o lado esquerdo é nulo, é aceitável que a expressão seja bem tipada desde que o direito seja de um tipo válido.
		// Normalmente, se ambos tiverem tipos, eles devem ser compatíveis.
		if (tipoEsq.eIgual(TipoPrimitivo.NULO)) {
			return tipoDir.eValido();
		}
		
		// vamos checar se são iguais ou se um deles é NULO
		return tipoEsq.eIgual(tipoDir) || tipoEsq.eIgual(TipoPrimitivo.NULO) || tipoDir.eIgual(TipoPrimitivo.NULO);
	}

	/**
	 * Retorna os tipos possiveis desta expressao.
	 */
	@Override
	public Tipo getTipo(AmbienteCompilacao ambiente) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		Tipo tipoEsq = getEsq().getTipo(ambiente);
		if (tipoEsq.eIgual(TipoPrimitivo.NULO)) {
			return getDir().getTipo(ambiente);
		}
		return tipoEsq;
	}

	@Override
	public ExpBinaria clone() {
		return new ExpNullCoalescing(getEsq().clone(), getDir().clone());
	}
}
