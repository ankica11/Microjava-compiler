// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class FormParsDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private String paramName;
    private ArrayDecl ArrayDecl;

    public FormParsDecl (Type Type, String paramName, ArrayDecl ArrayDecl) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.paramName=paramName;
        this.ArrayDecl=ArrayDecl;
        if(ArrayDecl!=null) ArrayDecl.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName=paramName;
    }

    public ArrayDecl getArrayDecl() {
        return ArrayDecl;
    }

    public void setArrayDecl(ArrayDecl ArrayDecl) {
        this.ArrayDecl=ArrayDecl;
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
        if(Type!=null) Type.accept(visitor);
        if(ArrayDecl!=null) ArrayDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ArrayDecl!=null) ArrayDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ArrayDecl!=null) ArrayDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+paramName);
        buffer.append("\n");

        if(ArrayDecl!=null)
            buffer.append(ArrayDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsDecl]");
        return buffer.toString();
    }
}
