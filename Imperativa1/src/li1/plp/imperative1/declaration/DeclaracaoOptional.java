package li1.plp.imperative1.declaration;

import li1.plp.expressions1.util.TipoOptional;
import li1.plp.expressions1.util.TipoPrimitivo;
import li1.plp.expressions2.expression.Expressao;
import li1.plp.expressions2.expression.Id;
import li1.plp.expressions2.memory.IdentificadorJaDeclaradoException;
import li1.plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import li1.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li1.plp.imperative1.memory.AmbienteExecucaoImperativa;

public class DeclaracaoOptional extends DeclaracaoVariavel {

    public DeclaracaoOptional(Id id, Expressao expressao) {
        super(id, expressao);
    }

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa ambiente)
            throws IdentificadorJaDeclaradoException,
            IdentificadorNaoDeclaradoException {
        boolean result = getExpressao().checaTipo(ambiente);
        if (result) {
            li1.plp.expressions1.util.Tipo t = getExpressao().getTipo(ambiente);
            
            // Se a expressão já é do tipo opcional (ex: vindo do ternário), usamos ela diretamente.
            // Caso contrário, envolvemos o tipo em um novo TipoOptional.
            li1.plp.expressions1.util.Tipo tipoFinal = 
                (t instanceof li1.plp.expressions1.util.TipoOptional) ? t : new li1.plp.expressions1.util.TipoOptional(t);
                
            ambiente.map(getId(), tipoFinal);
        }
        return result;
    }
}
