// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclAdditionals extends ConstDeclAdditional {

    private ConstDeclAdditional ConstDeclAdditional;
    private ConstDeclOne ConstDeclOne;

    public ConstDeclAdditionals (ConstDeclAdditional ConstDeclAdditional, ConstDeclOne ConstDeclOne) {
        this.ConstDeclAdditional=ConstDeclAdditional;
        if(ConstDeclAdditional!=null) ConstDeclAdditional.setParent(this);
        this.ConstDeclOne=ConstDeclOne;
        if(ConstDeclOne!=null) ConstDeclOne.setParent(this);
    }

    public ConstDeclAdditional getConstDeclAdditional() {
        return ConstDeclAdditional;
    }

    public void setConstDeclAdditional(ConstDeclAdditional ConstDeclAdditional) {
        this.ConstDeclAdditional=ConstDeclAdditional;
    }

    public ConstDeclOne getConstDeclOne() {
        return ConstDeclOne;
    }

    public void setConstDeclOne(ConstDeclOne ConstDeclOne) {
        this.ConstDeclOne=ConstDeclOne;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclAdditional!=null) ConstDeclAdditional.accept(visitor);
        if(ConstDeclOne!=null) ConstDeclOne.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclAdditional!=null) ConstDeclAdditional.traverseTopDown(visitor);
        if(ConstDeclOne!=null) ConstDeclOne.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclAdditional!=null) ConstDeclAdditional.traverseBottomUp(visitor);
        if(ConstDeclOne!=null) ConstDeclOne.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclAdditionals(\n");

        if(ConstDeclAdditional!=null)
            buffer.append(ConstDeclAdditional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclOne!=null)
            buffer.append(ConstDeclOne.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclAdditionals]");
        return buffer.toString();
    }
}
