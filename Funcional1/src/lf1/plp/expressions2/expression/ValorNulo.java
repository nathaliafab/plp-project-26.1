package lf1.plp.expressions2.expression;

import lf1.plp.expressions1.util.Tipo;
import lf1.plp.expressions1.util.TipoPrimitivo;
import lf1.plp.expressions2.memory.AmbienteCompilacao;

/**
 * Este valor primitivo encapsula um Nulo.
 */
public class ValorNulo extends ValorConcreto {

	/**
	 * cria um objeto encapsulando nulo
	 */
	public ValorNulo() {
		super(null);
	}

	/**
	 * Retorna os tipos possiveis desta expressao.
	 * 
	 * @param amb
	 *            o ambiente de compilação.
	 * @return os tipos possiveis desta expressao.
	 */
	public Tipo getTipo(AmbienteCompilacao amb) {
		return TipoPrimitivo.NULO;
	}

	@Override
	public String toString() {
		return String.format("\"%s\"", super.toString());
	}
	
	public ValorNulo clone() {
		return new ValorNulo();
	}
}
