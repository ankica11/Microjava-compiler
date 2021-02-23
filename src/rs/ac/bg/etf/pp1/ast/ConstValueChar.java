// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class ConstValueChar extends ConstValue {

    private String value;

    public ConstValueChar (String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstValueChar(\n");

        buffer.append(" "+tab+value);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstValueChar]");
        return buffer.toString();
    }
}
