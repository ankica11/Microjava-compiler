// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class VarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private VarIdent VarIdent;
    private VarDeclAdditional VarDeclAdditional;

    public VarDecl (Type Type, VarIdent VarIdent, VarDeclAdditional VarDeclAdditional) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarIdent=VarIdent;
        if(VarIdent!=null) VarIdent.setParent(this);
        this.VarDeclAdditional=VarDeclAdditional;
        if(VarDeclAdditional!=null) VarDeclAdditional.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarIdent getVarIdent() {
        return VarIdent;
    }

    public void setVarIdent(VarIdent VarIdent) {
        this.VarIdent=VarIdent;
    }

    public VarDeclAdditional getVarDeclAdditional() {
        return VarDeclAdditional;
    }

    public void setVarDeclAdditional(VarDeclAdditional VarDeclAdditional) {
        this.VarDeclAdditional=VarDeclAdditional;
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
        if(VarIdent!=null) VarIdent.accept(visitor);
        if(VarDeclAdditional!=null) VarDeclAdditional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarIdent!=null) VarIdent.traverseTopDown(visitor);
        if(VarDeclAdditional!=null) VarDeclAdditional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarIdent!=null) VarIdent.traverseBottomUp(visitor);
        if(VarDeclAdditional!=null) VarDeclAdditional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarIdent!=null)
            buffer.append(VarIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclAdditional!=null)
            buffer.append(VarDeclAdditional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDecl]");
        return buffer.toString();
    }
}
