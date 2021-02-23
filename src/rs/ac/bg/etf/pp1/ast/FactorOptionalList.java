// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class FactorOptionalList extends FactorOptList {

    private FactorOptList FactorOptList;
    private Mulop Mulop;
    private Factor Factor;

    public FactorOptionalList (FactorOptList FactorOptList, Mulop Mulop, Factor Factor) {
        this.FactorOptList=FactorOptList;
        if(FactorOptList!=null) FactorOptList.setParent(this);
        this.Mulop=Mulop;
        if(Mulop!=null) Mulop.setParent(this);
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
    }

    public FactorOptList getFactorOptList() {
        return FactorOptList;
    }

    public void setFactorOptList(FactorOptList FactorOptList) {
        this.FactorOptList=FactorOptList;
    }

    public Mulop getMulop() {
        return Mulop;
    }

    public void setMulop(Mulop Mulop) {
        this.Mulop=Mulop;
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FactorOptList!=null) FactorOptList.accept(visitor);
        if(Mulop!=null) Mulop.accept(visitor);
        if(Factor!=null) Factor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorOptList!=null) FactorOptList.traverseTopDown(visitor);
        if(Mulop!=null) Mulop.traverseTopDown(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorOptList!=null) FactorOptList.traverseBottomUp(visitor);
        if(Mulop!=null) Mulop.traverseBottomUp(visitor);
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorOptionalList(\n");

        if(FactorOptList!=null)
            buffer.append(FactorOptList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mulop!=null)
            buffer.append(Mulop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorOptionalList]");
        return buffer.toString();
    }
}
