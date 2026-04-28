package li1.plp.imperative1.command;

import li1.plp.expressions2.expression.Expressao;
import li1.plp.expressions2.expression.Id;
import li1.plp.expressions2.memory.VariavelJaDeclaradaException;
import li1.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li1.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li1.plp.imperative1.memory.AmbienteExecucaoImperativa;

public class Atribuicao implements Comando {

	private Id id;

	private Expressao expressao;

	public Atribuicao(Id id, Expressao expressao) {
		this.id = id;
		this.expressao = expressao;
	}

	/**
	 * Executa a atribui��o.
	 * 
	 * @param ambiente
	 *            o ambiente que contem o mapeamento entre identificadores e
	 *            valores.
	 * 
	 * @return o ambiente modificado pela execu��o da atribui��o.
	 * 
	 */
	public AmbienteExecucaoImperativa executar(
			AmbienteExecucaoImperativa ambiente)
			throws VariavelJaDeclaradaException, VariavelNaoDeclaradaException {
		ambiente.changeValor(id, expressao.avaliar(ambiente));
		return ambiente;
	}

	/**
	 * Um comando de atribui��o est� bem tipado, se o tipo do identificador � o
	 * mesmo da express�o. O tipo de um identificador � determinado pelo tipo da
	 * express�o que o inicializou (na declara��o).
	 * 
	 * @param ambiente
	 *            o ambiente que contem o mapeamento entre identificadores e
	 *            valores.
	 * 
	 * @return <code>true</code> se os tipos da atribui��o s�o v�lidos;
	 *         <code>false</code> caso contrario.
	 * 
	 */
	public boolean checaTipo(AmbienteCompilacaoImperativa ambiente)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		boolean isExpNull = expressao.getTipo(ambiente).eIgual(li1.plp.expressions1.util.TipoPrimitivo.NULO);
		boolean isIdOptional = id.getTipo(ambiente) instanceof li1.plp.expressions1.util.TipoOptional;
		if (isExpNull && !isIdOptional) {
			return false;
		}
		if (isExpNull && isIdOptional) {
			return expressao.checaTipo(ambiente);
		}
		boolean ok = expressao.checaTipo(ambiente)
				&& id.getTipo(ambiente).eIgual(expressao.getTipo(ambiente));
		if (!ok) {
			System.out.println("Atribuicao falhou o checaTipo! id type: " + id.getTipo(ambiente).getNome() + ", exp type: " + expressao.getTipo(ambiente).getNome());
		}
		return ok;
	}

}
