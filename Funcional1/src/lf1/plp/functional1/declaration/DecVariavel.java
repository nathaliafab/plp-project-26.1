package lf1.plp.functional1.declaration;


import java.util.Map;

import lf1.plp.expressions1.util.Tipo;
import lf1.plp.expressions2.expression.Expressao;
import lf1.plp.expressions2.expression.Id;
import lf1.plp.expressions2.expression.Valor;
import lf1.plp.expressions2.memory.AmbienteCompilacao;
import lf1.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf1.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf1.plp.functional1.memory.AmbienteExecucaoFuncional;
import lf1.plp.functional1.util.DefFuncao;

public class DecVariavel implements DeclaracaoFuncional {
        private Id id;
        private Expressao expressao;
        private boolean isOptional;

        public DecVariavel(Id idArg, Expressao expressaoArg, boolean isOptional) {
                id = idArg;
                expressao = expressaoArg;
                this.isOptional = isOptional;
        }

        public DecVariavel(Id idArg, Expressao expressaoArg) {
                id = idArg;
                expressao = expressaoArg;
                this.isOptional = false;
        }

	/**
	 * Retorna uma representacao String desta expressao. Util para depuracao.
	 * 
	 * @return uma representacao String desta expressao.
	 */
        @Override
        public String toString() {
                return String.format("var %s = %s", id, expressao);
        }

        public Expressao getExpressao() {
                return expressao;
        }

        public Id getId() {
                return id;
        }

	/**
	 * Retorna os tipos possiveis desta declara��o.
	 * 
	 * @param amb
	 *            o ambiente que contem o mapeamento entre identificadores e
	 *            tipos.
	 * @return os tipos possiveis desta declara��o.
	 * @exception VariavelNaoDeclaradaException
	 *                se houver uma vari&aacute;vel n&atilde;o declarada no
	 *                ambiente.
	 * @exception VariavelJaDeclaradaException
	 *                se houver uma mesma vari&aacute;vel declarada duas vezes
	 *                no mesmo bloco do ambiente.
	 * @precondition this.checaTipo(amb);
	 */
        public Tipo getTipo(AmbienteCompilacao amb)
                        throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
                return expressao.getTipo(amb);
        }

	/**
	 * Realiza a verificacao de tipos desta declara��o.
	 * 
	 * @param amb
	 *            o ambiente de compilação.
	 * @return <code>true</code> se os tipos da expressao sao validos;
	 *         <code>false</code> caso contrario.
	 * @exception VariavelNaoDeclaradaException
	 *                se existir um identificador nao declarado no ambiente.
	 * @exception VariavelNaoDeclaradaException
	 *                se existir um identificador declarado mais de uma vez no
	 *                mesmo bloco do ambiente.
	 */
        public boolean checaTipo(AmbienteCompilacao ambiente)
                        throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
                boolean result = expressao.checaTipo(ambiente);
                if (result) {
                        Tipo tipo = expressao.getTipo(ambiente);
                        if (!isOptional && !ambiente.isAlreadyOptional(getId()) && tipo.eNulo()) {
                                return false; // Nao pode ser nulo se nao for opcional
                        }
                }
                return result;
        }

        public DecVariavel clone() {
                return new DecVariavel(this.id.clone(), this.expressao.clone(), this.isOptional);
        }

        public void elabora(AmbienteCompilacao amb, AmbienteCompilacao aux) throws VariavelJaDeclaradaException {
                aux.map(getId(), getTipo(amb), isOptional);
        }

        public void incluir(AmbienteCompilacao amb, AmbienteCompilacao aux) throws VariavelJaDeclaradaException {
                amb.map(getId(), aux.get(getId()), isOptional);
        }

        public void elabora(AmbienteExecucaoFuncional amb, AmbienteExecucaoFuncional aux) throws VariavelJaDeclaradaException {
                Valor valor = getExpressao().avaliar(amb);
                if (!isOptional && !amb.isAlreadyOptional(getId()) && !aux.isAlreadyOptional(getId()) && (valor instanceof lf1.plp.expressions2.expression.ValorNulo)) {
                        throw new lf1.plp.expressions1.excecao.ErroTipoException();
                }
                aux.map(getId(), valor, isOptional);
        }

        public void incluir(AmbienteExecucaoFuncional amb, AmbienteExecucaoFuncional aux) throws VariavelJaDeclaradaException {
                amb.map(getId(), aux.get(getId()), isOptional);
        }

}
