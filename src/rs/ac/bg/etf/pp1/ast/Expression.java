// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class Expression extends Expr1 {

    private ExprOptList ExprOptList;

    public Expression (ExprOptList ExprOptList) {
        this.ExprOptList=ExprOptList;
        if(ExprOptList!=null) ExprOptList.setParent(this);
    }

    public ExprOptList getExprOptList() {
        return ExprOptList;
    }

    public void setExprOptList(ExprOptList ExprOptList) {
        this.ExprOptList=ExprOptList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprOptList!=null) ExprOptList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprOptList!=null) ExprOptList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprOptList!=null) ExprOptList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expression(\n");

        if(ExprOptList!=null)
            buffer.append(ExprOptList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expression]");
        return buffer.toString();
    }
}
