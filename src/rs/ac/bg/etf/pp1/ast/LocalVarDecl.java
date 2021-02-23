// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class LocalVarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private LocalVarIdent LocalVarIdent;
    private LocalVarDeclAdditional LocalVarDeclAdditional;

    public LocalVarDecl (Type Type, LocalVarIdent LocalVarIdent, LocalVarDeclAdditional LocalVarDeclAdditional) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.LocalVarIdent=LocalVarIdent;
        if(LocalVarIdent!=null) LocalVarIdent.setParent(this);
        this.LocalVarDeclAdditional=LocalVarDeclAdditional;
        if(LocalVarDeclAdditional!=null) LocalVarDeclAdditional.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public LocalVarIdent getLocalVarIdent() {
        return LocalVarIdent;
    }

    public void setLocalVarIdent(LocalVarIdent LocalVarIdent) {
        this.LocalVarIdent=LocalVarIdent;
    }

    public LocalVarDeclAdditional getLocalVarDeclAdditional() {
        return LocalVarDeclAdditional;
    }

    public void setLocalVarDeclAdditional(LocalVarDeclAdditional LocalVarDeclAdditional) {
        this.LocalVarDeclAdditional=LocalVarDeclAdditional;
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
        if(Type!=null) Type.accept(visitor);
        if(LocalVarIdent!=null) LocalVarIdent.accept(visitor);
        if(LocalVarDeclAdditional!=null) LocalVarDeclAdditional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(LocalVarIdent!=null) LocalVarIdent.traverseTopDown(visitor);
        if(LocalVarDeclAdditional!=null) LocalVarDeclAdditional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(LocalVarIdent!=null) LocalVarIdent.traverseBottomUp(visitor);
        if(LocalVarDeclAdditional!=null) LocalVarDeclAdditional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("LocalVarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LocalVarIdent!=null)
            buffer.append(LocalVarIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(LocalVarDeclAdditional!=null)
            buffer.append(LocalVarDeclAdditional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [LocalVarDecl]");
        return buffer.toString();
    }
}
