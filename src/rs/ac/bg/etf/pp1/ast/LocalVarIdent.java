// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class LocalVarIdent implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String localVarName;
    private ArrayDecl ArrayDecl;

    public LocalVarIdent (String localVarName, ArrayDecl ArrayDecl) {
        this.localVarName=localVarName;
        this.ArrayDecl=ArrayDecl;
        if(ArrayDecl!=null) ArrayDecl.setParent(this);
    }

    public String getLocalVarName() {
        return localVarName;
    }

    public void setLocalVarName(String localVarName) {
        this.localVarName=localVarName;
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
        if(ArrayDecl!=null) ArrayDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrayDecl!=null) ArrayDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrayDecl!=null) ArrayDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("LocalVarIdent(\n");

        buffer.append(" "+tab+localVarName);
        buffer.append("\n");

        if(ArrayDecl!=null)
            buffer.append(ArrayDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [LocalVarIdent]");
        return buffer.toString();
    }
}
