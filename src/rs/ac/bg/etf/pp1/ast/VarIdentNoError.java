// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class VarIdentNoError extends VarIdent {

    private String varName;
    private ArrayDecl ArrayDecl;

    public VarIdentNoError (String varName, ArrayDecl ArrayDecl) {
        this.varName=varName;
        this.ArrayDecl=ArrayDecl;
        if(ArrayDecl!=null) ArrayDecl.setParent(this);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public ArrayDecl getArrayDecl() {
        return ArrayDecl;
    }

    public void setArrayDecl(ArrayDecl ArrayDecl) {
        this.ArrayDecl=ArrayDecl;
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
        buffer.append("VarIdentNoError(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(ArrayDecl!=null)
            buffer.append(ArrayDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarIdentNoError]");
        return buffer.toString();
    }
}
