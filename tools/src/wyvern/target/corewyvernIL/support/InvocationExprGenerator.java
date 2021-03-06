package wyvern.target.corewyvernIL.support;

import java.util.List;

import wyvern.target.corewyvernIL.decltype.DeclType;
import wyvern.target.corewyvernIL.decltype.DefDeclType;
import wyvern.target.corewyvernIL.decltype.ValDeclType;
import wyvern.target.corewyvernIL.decltype.VarDeclType;
import wyvern.target.corewyvernIL.expression.Expression;
import wyvern.target.corewyvernIL.expression.FieldGet;
import wyvern.target.corewyvernIL.expression.IExpr;
import wyvern.target.corewyvernIL.expression.MethodCall;
import wyvern.target.corewyvernIL.expression.Variable;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.tools.errors.ErrorMessage;
import wyvern.tools.errors.FileLocation;
import wyvern.tools.errors.HasLocation;
import wyvern.tools.errors.ToolError;

public class InvocationExprGenerator implements CallableExprGenerator {
    private final IExpr receiver;
    private final DeclType declType;
    private final FileLocation location;

    public InvocationExprGenerator(IExpr iExpr, String operationName, GenContext ctx, FileLocation loc) {
        this.receiver = iExpr;
        this.location = loc;

        ValueType receiverType = iExpr.typeCheck(ctx, null);

        if (Util.isDynamicType(receiverType)) {
            this.declType = null;
            return;
        }

        List<DeclType> dts = receiverType.findDecls(operationName, ctx);
        // not interested in finding Type Decls (abstract or not)
        dts.removeIf(cdt -> cdt.isTypeOrEffectDecl());
        if (dts.size() == 0) {
            boolean startsWith = iExpr.toString().startsWith("MOD$");
            DeclType applyDecl = receiverType.findDecl("apply", ctx);
            if (iExpr instanceof Variable && startsWith && applyDecl != null) {
                // treating a module def as if it were a module
                ToolError.reportError(ErrorMessage.MUST_INSTANTIATE, loc, ((Variable) iExpr).getName().substring(4));
            }
            ToolError.reportError(ErrorMessage.NO_SUCH_METHOD, loc, operationName, receiverType.desugar(ctx));
        }
        if (dts.size() > 1) {
            ToolError.reportError(ErrorMessage.DUPLICATE_MEMBER, loc, receiverType.toString(), operationName);
        }
        DeclType dt = dts.get(0);
        declType = dt.adapt(View.from(iExpr, ctx));
    }

    @Override
    public Expression genExpr(FileLocation loc) {
        if (declType instanceof ValDeclType || declType instanceof VarDeclType) {
            return new FieldGet(receiver.locationHint(loc), declType.getName(), location);
        } else {
            ToolError.reportError(ErrorMessage.METHODS_MUST_BE_INVOKED, location);
            return null;
        }
    }

    @Override
    public IExpr genExprWithArgs(List<? extends IExpr> args, HasLocation loc) {
        if (declType instanceof ValDeclType || declType instanceof VarDeclType) {
            IExpr e = genExpr(loc.getLocation());
            return new MethodCall(e, Util.APPLY_NAME, args, loc);
        } else {
            return new MethodCall(receiver, declType.getName(), args, loc);
        }
    }

    @Override
    public DefDeclType getDeclType(TypeContext ctx) {
        if (declType == null) {
            return null;
        } else if (declType instanceof ValDeclType || declType instanceof VarDeclType) {
            Expression e = genExpr(null);
            ValueType vt = e.typeCheck(ctx, null);
            return (DefDeclType) vt.findDecl(Util.APPLY_NAME, ctx);
            // return (DefDeclType)vt.findDecl(Util.APPLY_NAME, ctx).adapt(View.from(receiver, ctx));
        } else if (declType instanceof DefDeclType) {
            return (DefDeclType) declType;
        } else {
            ToolError.reportError(ErrorMessage.NOT_A_METHOD, location, declType.getName());
            return null;
        }
    }
}
