// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class ExpressionOptList extends ExprOptList {

    private ExprOptList ExprOptList;
    private Addop Addop;
    private Term Term;

    public ExpressionOptList (ExprOptList ExprOptList, Addop Addop, Term Term) {
        this.ExprOptList=ExprOptList;
        if(ExprOptList!=null) ExprOptList.setParent(this);
        this.Addop=Addop;
        if(Addop!=null) Addop.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public ExprOptList getExprOptList() {
        return ExprOptList;
    }

    public void setExprOptList(ExprOptList ExprOptList) {
        this.ExprOptList=ExprOptList;
    }

    public Addop getAddop() {
        return Addop;
    }

    public void setAddop(Addop Addop) {
        this.Addop=Addop;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprOptList!=null) ExprOptList.accept(visitor);
        if(Addop!=null) Addop.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprOptList!=null) ExprOptList.traverseTopDown(visitor);
        if(Addop!=null) Addop.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprOptList!=null) ExprOptList.traverseBottomUp(visitor);
        if(Addop!=null) Addop.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExpressionOptList(\n");

        if(ExprOptList!=null)
            buffer.append(ExprOptList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Addop!=null)
            buffer.append(Addop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExpressionOptList]");
        return buffer.toString();
    }
}
