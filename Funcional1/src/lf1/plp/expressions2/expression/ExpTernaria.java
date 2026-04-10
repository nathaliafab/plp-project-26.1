package lf1.plp.expressions2.expression;

import lf1.plp.expressions2.memory.AmbienteCompilacao;
import lf1.plp.expressions2.memory.AmbienteExecucao;
import lf1.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf1.plp.expressions2.memory.VariavelNaoDeclaradaException;

/**
 * Uma expressao ternaria contem tres expressoes e dois operadores. Ha uma ordem
 * definida entre estas sub-expressoes
 */
public abstract class ExpTernaria implements Expressao {

	/**
	 * Expressao da esquerda
	 */
	protected Expressao esq;

	/**
	 * Expressao do meio
	 */
	protected Expressao mei;

	/**
	 * Expressao da direita
	 */
	protected Expressao dir;

	/**
	 * Operador 1 desta expressao ternaria
	 */
	private String operador1;

	/**
	 * Operador 1 desta expressao ternaria
	 */
	private String operador2;

	/**
	 * Construtor da classe.
	 * 
	 * @param esq
	 *            a expressao da esquerda.
	 * @param mei
	 *            a expressao do meio.
	 * @param dir
	 *            a expressao da direita.
	 * @param operador1
	 *            o operador1 desta expressao ternaria.
	 * @param operador2
	 *            o operador2 desta expressao ternaria.
	 */
	public ExpTernaria(Expressao esq, Expressao mei, Expressao dir, String operador1, String operador2) {
		this.esq = esq;
		this.mei = mei;
		this.dir = dir;
		this.operador1 = operador1;
		this.operador2 = operador2;
	}

	/**
	 * Retorna a expressao da esquerda
	 * 
	 * @return a expressao da esquerda
	 */
	public Expressao getEsq() {
		return esq;
	}

	/**
	 * Retorna a expressao do meio
	 * 
	 * @return a expressao do meio
	 */
	public Expressao getMei() {
		return mei;
	}

	/**
	 * Retorna a expressao da direita
	 * 
	 * @return a expressao da direita
	 */
	public Expressao getDir() {
		return dir;
	}

	/**
	 * Retorna o operador desta expressao ternaria
	 * 
	 * @return o operador desta expressao ternaria
	 */
	public String getOperador1() {
		return operador1;
	}

	/**
	 * Retorna o operador desta expressao ternaria
	 * 
	 * @return o operador desta expressao ternaria
	 */
	public String getOperador2() {
		return operador2;
	}

	/**
	 * Retorna uma representacao String desta expressao. Util para depuracao.
	 * 
	 * @return uma representacao String desta expressao.
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s %s %s", esq, operador1, mei, operador2, dir);
	}

	/**
	 * Realiza a verificacao de tipos desta expressao.
	 * 
	 * @param amb
	 *            o ambiente de compila��o.
	 * @return <code>true</code> se os tipos das subexpressoes sao validos;
	 *         <code>false</code> caso contrario.
	 * @exception VariavelJaDeclaradaException
	 *                se a vari�vel j� est� declarada no ambiente
	 * @exception VariavelNaoDeclaradaException
	 *                se a vari�vel ainda n�o foi declarada no ambiente.
	 */
	public boolean checaTipo(AmbienteCompilacao amb)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
		boolean result = true;
		if (!getEsq().checaTipo(amb) || !getMei().checaTipo(amb) || !getDir().checaTipo(amb)) {
			result = false;
		} else {
			result = this.checaTipoElementoTerminal(amb);
		}
		return result;
	}

	/**
	 * M�todo 'template' que ser� implementado nas subclasses para checar o tipo
	 * do head terminal
	 */
	protected abstract boolean checaTipoElementoTerminal(AmbienteCompilacao amb)
			throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException;
	
	
	public Expressao reduzir(AmbienteExecucao ambiente) {
		this.esq = this.esq.reduzir(ambiente);
		this.mei = this.mei.reduzir(ambiente);
		this.dir = this.dir.reduzir(ambiente);
		
		return this;
	}
	
	public abstract ExpTernaria clone();
}
