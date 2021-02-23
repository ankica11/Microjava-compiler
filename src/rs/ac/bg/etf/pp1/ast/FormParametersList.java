// generated with ast extension for cup
// version 0.8
// 5/1/2021 23:43:23


package rs.ac.bg.etf.pp1.ast;

public class FormParametersList extends FormParsList {

    private FormParsList FormParsList;
    private FormParsDecl FormParsDecl;

    public FormParametersList (FormParsList FormParsList, FormParsDecl FormParsDecl) {
        this.FormParsList=FormParsList;
        if(FormParsList!=null) FormParsList.setParent(this);
        this.FormParsDecl=FormParsDecl;
        if(FormParsDecl!=null) FormParsDecl.setParent(this);
    }

    public FormParsList getFormParsList() {
        return FormParsList;
    }

    public void setFormParsList(FormParsList FormParsList) {
        this.FormParsList=FormParsList;
    }

    public FormParsDecl getFormParsDecl() {
        return FormParsDecl;
    }

    public void setFormParsDecl(FormParsDecl FormParsDecl) {
        this.FormParsDecl=FormParsDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsList!=null) FormParsList.accept(visitor);
        if(FormParsDecl!=null) FormParsDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsList!=null) FormParsList.traverseTopDown(visitor);
        if(FormParsDecl!=null) FormParsDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsList!=null) FormParsList.traverseBottomUp(visitor);
        if(FormParsDecl!=null) FormParsDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParametersList(\n");

        if(FormParsList!=null)
            buffer.append(FormParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsDecl!=null)
            buffer.append(FormParsDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParametersList]");
        return buffer.toString();
    }
}
