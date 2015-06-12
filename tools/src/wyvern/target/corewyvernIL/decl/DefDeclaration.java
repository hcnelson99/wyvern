package wyvern.target.corewyvernIL.decl;

import java.util.List;

import wyvern.target.corewyvernIL.FormalArg;
import wyvern.target.corewyvernIL.emitIL;
import wyvern.target.corewyvernIL.astvisitor.EmitILVisitor;
import wyvern.target.corewyvernIL.expression.Expression;
import wyvern.target.corewyvernIL.type.Type;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.target.corewyvernIL.Environment;

public class DefDeclaration extends Declaration implements emitIL {

	private String methodName;
	private List<FormalArg> formalArgs;
	private ValueType type;
	private Expression body;
	
	public DefDeclaration(String methodName, List<FormalArg> formalArgs,
			ValueType type, Expression body) {
		super();
		this.methodName = methodName;
		this.formalArgs = formalArgs;
		this.type = type;
		this.body = body;
	}

	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public List<FormalArg> getFormalArgs() {
		return formalArgs;
	}
	
	public void setFormalArgs(List<FormalArg> formalArgs) {
		this.formalArgs = formalArgs;
	}
	
	public ValueType getType() {
		return type;
	}
	public void setType(ValueType type) {
		this.type = type;
	}
	
	public Expression getBody() {
		return body;
	}
	public void setBody(Expression body) {
		this.body = body;
	}

	@Override
	public Type typeCheck(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String acceptEmitILVisitor(EmitILVisitor emitILVisitor,
			Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
