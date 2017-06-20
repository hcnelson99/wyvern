package wyvern.target.corewyvernIL.decl;

import java.io.IOException;
import java.util.Set;

import wyvern.target.corewyvernIL.astvisitor.ASTVisitor;
import wyvern.target.corewyvernIL.decltype.DeclType;
import wyvern.target.corewyvernIL.decltype.EffectDeclType;
import wyvern.target.corewyvernIL.expression.Expression;
import wyvern.target.corewyvernIL.expression.IExpr;
import wyvern.target.corewyvernIL.support.EvalContext;
import wyvern.target.corewyvernIL.support.TypeContext;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.tools.errors.FileLocation;

public class EffectDeclaration extends DeclarationWithRHS {

	/*@Override
	public String toString() {
		return "ValDeclaration[" + getName() + " : " + type + " = " + getDefinition() + "]";
	}*/

	public EffectDeclaration(String fieldName, ValueType type, IExpr iExpr, FileLocation loc) {
		super(fieldName, iExpr, loc);
		this.type = type;
	}

	private ValueType type;

	@Override
	public boolean containsResource(TypeContext ctx) {
		return type.isResource(ctx);
	}

	@Override
	public ValueType getType() {
		return type;
	}

	@Override
	public void doPrettyPrint(Appendable dest, String indent) throws IOException {
		dest.append(indent).append("effect ").append(getName()).append(':');
		type.doPrettyPrint(dest, indent);
		dest.append(" = ");
		getDefinition().doPrettyPrint(dest, indent);
		dest.append('\n');
	}

	@Override
	public <S, T> T acceptVisitor(ASTVisitor <S, T> emitILVisitor,
			S state) {
		return emitILVisitor.visit(state, this);
	}

	@Override
	public DeclType getDeclType() {
		return new EffectDeclType(getName(), type);
	}

	@Override
	public Declaration interpret(EvalContext ctx) {
		Expression newValue = (Expression) getDefinition().interpret(ctx);
		return new EffectDeclaration(getName(), type, newValue, getLocation());
	}

	public Set<String> getFreeVariables() {
		return getDefinition().getFreeVariables();
	}

}
