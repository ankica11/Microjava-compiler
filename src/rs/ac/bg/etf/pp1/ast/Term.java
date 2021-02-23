// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class Term implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private FactorOptList FactorOptList;

    public Term (FactorOptList FactorOptList) {
        this.FactorOptList=FactorOptList;
        if(FactorOptList!=null) FactorOptList.setParent(this);
    }

    public FactorOptList getFactorOptList() {
        return FactorOptList;
    }

    public void setFactorOptList(FactorOptList FactorOptList) {
        this.FactorOptList=FactorOptList;
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
        if(FactorOptList!=null) FactorOptList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorOptList!=null) FactorOptList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorOptList!=null) FactorOptList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Term(\n");

        if(FactorOptList!=null)
            buffer.append(FactorOptList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Term]");
        return buffer.toString();
    }
}
