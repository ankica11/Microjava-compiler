// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class MethodTypeName implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private RetValType RetValType;
    private String methodName;

    public MethodTypeName (RetValType RetValType, String methodName) {
        this.RetValType=RetValType;
        if(RetValType!=null) RetValType.setParent(this);
        this.methodName=methodName;
    }

    public RetValType getRetValType() {
        return RetValType;
    }

    public void setRetValType(RetValType RetValType) {
        this.RetValType=RetValType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName=methodName;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RetValType!=null) RetValType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RetValType!=null) RetValType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RetValType!=null) RetValType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodTypeName(\n");

        if(RetValType!=null)
            buffer.append(RetValType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+methodName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodTypeName]");
        return buffer.toString();
    }
}
