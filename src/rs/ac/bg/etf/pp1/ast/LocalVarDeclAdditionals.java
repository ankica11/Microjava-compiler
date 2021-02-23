// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class LocalVarDeclAdditionals extends LocalVarDeclAdditional {

    private LocalVarDeclAdditional LocalVarDeclAdditional;
    private LocalVarIdent LocalVarIdent;

    public LocalVarDeclAdditionals (LocalVarDeclAdditional LocalVarDeclAdditional, LocalVarIdent LocalVarIdent) {
        this.LocalVarDeclAdditional=LocalVarDeclAdditional;
        if(LocalVarDeclAdditional!=null) LocalVarDeclAdditional.setParent(this);
        this.LocalVarIdent=LocalVarIdent;
        if(LocalVarIdent!=null) LocalVarIdent.setParent(this);
    }

    public LocalVarDeclAdditional getLocalVarDeclAdditional() {
        return LocalVarDeclAdditional;
    }

    public void setLocalVarDeclAdditional(LocalVarDeclAdditional LocalVarDeclAdditional) {
        this.LocalVarDeclAdditional=LocalVarDeclAdditional;
    }

    public LocalVarIdent getLocalVarIdent() {
        return LocalVarIdent;
    }

    public void setLocalVarIdent(LocalVarIdent LocalVarIdent) {
        this.LocalVarIdent=LocalVarIdent;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LocalVarDeclAdditional!=null) LocalVarDeclAdditional.accept(visitor);
        if(LocalVarIdent!=null) LocalVarIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LocalVarDeclAdditional!=null) LocalVarDeclAdditional.traverseTopDown(visitor);
        if(LocalVarIdent!=null) LocalVarIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LocalVarDeclAdditional!=null) LocalVarDeclAdditional.traverseBottomUp(visitor);
        if(LocalVarIdent!=null) LocalVarIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("LocalVarDeclAdditionals(\n");

        if(LocalVarDeclAdditional!=null)
            buffer.append(LocalVarDeclAdditional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LocalVarIdent!=null)
            buffer.append(LocalVarIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [LocalVarDeclAdditionals]");
        return buffer.toString();
    }
}
