package lf1.plp.expressions2.memory;

import lf1.plp.expressions2.expression.Id;


public interface Ambiente<T> {

	public void incrementa();

	public void restaura();

    public boolean isAlreadyOptional(Id idArg);

	public void map(Id idArg, T tipoId) throws VariavelJaDeclaradaException;

	public void map(Id idArg, T tipoId, boolean optional) throws VariavelJaDeclaradaException;

	public T get(Id idArg) throws VariavelNaoDeclaradaException;

}
