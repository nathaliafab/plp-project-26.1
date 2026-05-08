package li1.plp.imperative1.command;

import li1.plp.expressions2.expression.Expressao;
import li1.plp.expressions2.expression.Id;
import li1.plp.expressions2.expression.ValorNulo;
import li1.plp.expressions2.memory.VariavelJaDeclaradaException;
import li1.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li1.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li1.plp.imperative1.memory.AmbienteExecucaoImperativa;

public class AtribuicaoSeNulo implements Comando {

    private Id id;
    private Expressao expressao;

    public AtribuicaoSeNulo(Id id, Expressao expressao) {
        this.id = id;
        this.expressao = expressao;
    }

    public AmbienteExecucaoImperativa executar(
            AmbienteExecucaoImperativa ambiente)
            throws VariavelJaDeclaradaException, VariavelNaoDeclaradaException {
        // só atribui se o valor atual for nulo
        if (ambiente.get(id) instanceof ValorNulo) {
            ambiente.changeValor(id, expressao.avaliar(ambiente));
        }
        return ambiente;
    }

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
            System.out.println("Atribuicao Null-Aware falhou o checaTipo! id type: " + id.getTipo(ambiente).getNome() + ", exp type: " + expressao.getTipo(ambiente).getNome());
        }
        return ok;
    }
}