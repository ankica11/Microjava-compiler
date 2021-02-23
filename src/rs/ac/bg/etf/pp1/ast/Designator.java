// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class Designator implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String name;
    private DesignatorOptionals DesignatorOptionals;

    public Designator (String name, DesignatorOptionals DesignatorOptionals) {
        this.name=name;
        this.DesignatorOptionals=DesignatorOptionals;
        if(DesignatorOptionals!=null) DesignatorOptionals.setParent(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public DesignatorOptionals getDesignatorOptionals() {
        return DesignatorOptionals;
    }

    public void setDesignatorOptionals(DesignatorOptionals DesignatorOptionals) {
        this.DesignatorOptionals=DesignatorOptionals;
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
        if(DesignatorOptionals!=null) DesignatorOptionals.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorOptionals!=null) DesignatorOptionals.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorOptionals!=null) DesignatorOptionals.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator(\n");

        buffer.append(" "+tab+name);
        buffer.append("\n");

        if(DesignatorOptionals!=null)
            buffer.append(DesignatorOptionals.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator]");
        return buffer.toString();
    }
}
