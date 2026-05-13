package li1.plp.imperative1.command;

import li1.plp.expressions2.expression.Expressao;
import li1.plp.expressions2.expression.ValorBooleano;
import li1.plp.expressions2.memory.IdentificadorJaDeclaradoException;
import li1.plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import li1.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li1.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li1.plp.imperative1.memory.EntradaVaziaException;
import li1.plp.imperative1.memory.ErroTipoEntradaException;

public class IfThenElse implements Comando {

	private Expressao expressao;

	private Comando comandoThen;

	private Comando comandoElse;

	public IfThenElse(Expressao expressao, Comando comandoThen,
			Comando comandoElse) {
		this.expressao = expressao;
		this.comandoThen = comandoThen;
		this.comandoElse = comandoElse;
	}

	/**
	 * Implementa o comando <code>if then else</code>.
	 * 
	 * @param ambiente
	 *            o ambiente de execu��o.
	 * 
	 * @return o ambiente depois de modificado pela execu��o do comando
	 *         <code>if then else</code>.
	 * @throws ErroTipoEntradaException 
	 * 
	 */
	public AmbienteExecucaoImperativa executar(
			AmbienteExecucaoImperativa ambiente)
			throws IdentificadorJaDeclaradoException,
			IdentificadorNaoDeclaradoException, EntradaVaziaException, ErroTipoEntradaException {
		if (((ValorBooleano) expressao.avaliar(ambiente)).valor())
			return comandoThen.executar(ambiente);
		else
			return comandoElse.executar(ambiente);
	}

	/**
	 * Realiza a verificacao de tipos da express�o e dos comandos do comando
	 * <code>if then else</code>
	 * 
	 * @param ambiente
	 *            o ambiente de compila��o.
	 * @return <code>true</code> se a express�o e os comando s�o bem tipados;
	 *         <code>false</code> caso contrario.
	 */
	public boolean checaTipo(AmbienteCompilacaoImperativa ambiente)
			throws IdentificadorJaDeclaradoException,
			IdentificadorNaoDeclaradoException, EntradaVaziaException {
		
		boolean isWellTyped = expressao.checaTipo(ambiente) && expressao.getTipo(ambiente).eBooleano();
		if (!isWellTyped) return false;

		li1.plp.expressions2.expression.Id idToCast = null;
		boolean isNotEquals = false;

		if (expressao instanceof li1.plp.expressions2.expression.ExpNot) {
			li1.plp.expressions2.expression.Expressao inner = ((li1.plp.expressions2.expression.ExpNot) expressao).getExp();
			if (inner instanceof li1.plp.expressions2.expression.ExpEquals) {
				li1.plp.expressions2.expression.ExpEquals eq = (li1.plp.expressions2.expression.ExpEquals) inner;
				if (eq.getEsq() instanceof li1.plp.expressions2.expression.Id && eq.getDir() instanceof li1.plp.expressions2.expression.ValorNulo) {
					idToCast = (li1.plp.expressions2.expression.Id) eq.getEsq();
					isNotEquals = true;
				} else if (eq.getDir() instanceof li1.plp.expressions2.expression.Id && eq.getEsq() instanceof li1.plp.expressions2.expression.ValorNulo) {
					idToCast = (li1.plp.expressions2.expression.Id) eq.getDir();
					isNotEquals = true;
				}
			}
		} else if (expressao instanceof li1.plp.expressions2.expression.ExpEquals) {
			li1.plp.expressions2.expression.ExpEquals eq = (li1.plp.expressions2.expression.ExpEquals) expressao;
			if (eq.getEsq() instanceof li1.plp.expressions2.expression.Id && eq.getDir() instanceof li1.plp.expressions2.expression.ValorNulo) {
				idToCast = (li1.plp.expressions2.expression.Id) eq.getEsq();
				isNotEquals = false;
			} else if (eq.getDir() instanceof li1.plp.expressions2.expression.Id && eq.getEsq() instanceof li1.plp.expressions2.expression.ValorNulo) {
				idToCast = (li1.plp.expressions2.expression.Id) eq.getDir();
				isNotEquals = false;
			}
		}

		// Checa bloco Then
		ambiente.incrementa();
		boolean thenResult = false;
		try {
			if (idToCast != null && isNotEquals) {
				li1.plp.expressions1.util.Tipo tipo = ambiente.get(idToCast);
				if (tipo instanceof li1.plp.expressions1.util.TipoOptional) {
					li1.plp.expressions1.util.Tipo base = ((li1.plp.expressions1.util.TipoOptional) tipo).getBaseType();
					if (base != null && !base.getNome().equals("NULO")) {
						ambiente.map(idToCast, new li1.plp.expressions1.util.TipoOptionalRefinado(base));
					}
				}
			}
			thenResult = comandoThen.checaTipo(ambiente);
		} finally {
			ambiente.restaura();
		}

		// Checa bloco Else
		ambiente.incrementa();
		boolean elseResult = false;
		try {
			if (idToCast != null && !isNotEquals) {
				li1.plp.expressions1.util.Tipo tipo = ambiente.get(idToCast);
				if (tipo instanceof li1.plp.expressions1.util.TipoOptional) {
					li1.plp.expressions1.util.Tipo base = ((li1.plp.expressions1.util.TipoOptional) tipo).getBaseType();
					if (base != null && !base.getNome().equals("NULO")) {
						ambiente.map(idToCast, new li1.plp.expressions1.util.TipoOptionalRefinado(base));
					}
				}
			}
			elseResult = comandoElse.checaTipo(ambiente);
		} finally {
			ambiente.restaura();
		}

		return thenResult && elseResult;
	}

}
