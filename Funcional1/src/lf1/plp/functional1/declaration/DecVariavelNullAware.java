package lf1.plp.functional1.declaration;

import java.util.Map;

import lf1.plp.expressions1.excecao.ErroTipoException;
import lf1.plp.expressions1.util.Tipo;
import lf1.plp.expressions1.util.TipoPrimitivo;
import lf1.plp.expressions2.expression.Expressao;
import lf1.plp.expressions2.expression.Id;
import lf1.plp.expressions2.expression.Valor;
import lf1.plp.expressions2.expression.ValorNulo;
import lf1.plp.expressions2.memory.AmbienteCompilacao;
import lf1.plp.expressions2.memory.AmbienteExecucao;
import lf1.plp.expressions2.memory.VariavelJaDeclaradaException;
import lf1.plp.expressions2.memory.VariavelNaoDeclaradaException;
import lf1.plp.functional1.memory.AmbienteExecucaoFuncional;
import lf1.plp.functional1.util.DefFuncao;

public class DecVariavelNullAware implements DeclaracaoFuncional {
        private Id id;
        private Expressao expressao;
        private boolean isOptional;

        public DecVariavelNullAware(Id idArg, Expressao expressaoArg, boolean isOptional) {
                id = idArg;
                expressao = expressaoArg;
                this.isOptional = isOptional;
        }

        public DecVariavelNullAware(Id idArg, Expressao expressaoArg) {
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
                return String.format("var %s ??= %s", id, expressao);
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
        public Tipo getTipo(AmbienteCompilacao amb) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
                return amb.get(getId()); 
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
        public boolean checaTipo(AmbienteCompilacao ambiente) throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException {
                boolean exprValida = expressao.checaTipo(ambiente);
                if (!exprValida) return false;
                
                Tipo tVar = ambiente.get(getId());
                Tipo tExp = expressao.getTipo(ambiente);
                
                if (tVar == null) return false;
                
                return tVar.eIgual(tExp) || tVar.eNulo() || tExp.eNulo() || tVar.eIgual(TipoPrimitivo.NULO);
        }

        public DecVariavelNullAware clone() {
                return new DecVariavelNullAware(this.id.clone(), this.expressao.clone(), this.isOptional);
        }

        public void elabora(AmbienteCompilacao amb, AmbienteCompilacao aux) throws VariavelJaDeclaradaException {
                boolean isOpt = isOptional;
                try {
                        isOpt = amb.isAlreadyOptional(getId());
                } catch (Exception e) {}
                
                Tipo tipoVar;
                try {
                        tipoVar = amb.get(getId());
                } catch (VariavelNaoDeclaradaException e) {
                        throw new RuntimeException("Variavel " + getId() + " nao declarada");
                }
                
                aux.map(getId(), tipoVar, isOpt);
        }

        public void incluir(AmbienteCompilacao amb, AmbienteCompilacao aux) throws VariavelJaDeclaradaException {
                try {
                        amb.map(getId(), aux.get(getId()), isOptional);
                } catch(VariavelNaoDeclaradaException e) {
                        e.printStackTrace();
                }
        }

        public void elabora(AmbienteExecucaoFuncional amb, AmbienteExecucaoFuncional aux) throws VariavelJaDeclaradaException {
                boolean isOpt = isOptional;
                try {
                        isOpt = amb.isAlreadyOptional(getId());
                } catch (Exception e) {}
                
                Valor valorAntigo = null;
                try {
                        valorAntigo = (Valor) amb.get(getId());
                } catch (VariavelNaoDeclaradaException e) {
                        throw new RuntimeException("Variavel " + getId() + " nao declarada");
                }

                Valor novoValor;
                try {
                        if (valorAntigo instanceof ValorNulo) {
                                novoValor = getExpressao().avaliar(amb);
                        } else {
                                novoValor = valorAntigo;
                        }
                } catch (VariavelNaoDeclaradaException e) {
                        throw new RuntimeException(e);
                }

                aux.map(getId(), novoValor, isOpt);
        }

        public void incluir(AmbienteExecucaoFuncional amb, AmbienteExecucaoFuncional aux) throws VariavelJaDeclaradaException {
                try {
                        amb.map(getId(), aux.get(getId()), isOptional);
                } catch(VariavelNaoDeclaradaException e) {
                        e.printStackTrace();
                }
        }
}
